package edu.bot.controller;

import edu.bot.IntegrationTest;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.dao.ChatStatusDao;
import edu.bot.dao.TrackingDao;
import edu.bot.service.UpdatesProcessingService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import static edu.bot.domain.ChatStatus.AWAITING_COMMAND;
import static edu.bot.domain.ChatStatus.AWAITING_URL_TO_TRACK;
import static edu.bot.domain.ChatStatus.AWAITING_URL_TO_UNTRACK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* TODO: resolve issue with @Order not working.  */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UpdateControllerTest extends IntegrationTest {

    private static final String UPDATE_URL = "/update";
    private static final String CONTENT_TYPE = "application/json";

    private static final Long CHAT_ID = 42L;
    private static final String TRACKED_URL = "https://github.com/unters/java-course-tinkoff-pt2/pull/10";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UpdatesProcessingService updatesProcessingService;
    @Autowired
    private ChatStatusDao chatStatusDao;

    @MockBean
    private TrackingDao trackingDao;

    @BeforeEach
    public void resetInvocationsCount(WebApplicationContext webApplicationContext) {
        Mockito.reset(telegramClient, scrapperClient);
    }

    @Test
    @Order(1)
    public void update_01_unregisteredUserSendsNotStartCommand_newChatDoesNotAppearInDatabase() throws Exception {
        // given
        Mockito.when(telegramClient.sendMessage(any())).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));
        String body = readRequestBody("01-trash-update.json");

        // when
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isNull();
        mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isNull();
        verify(telegramClient, times(1)).sendMessage(any());
    }

    @Test
    @Order(2)
    public void update_02_unregisteredUserSendsStartCommand_newChatAppearsInDatabase() throws Exception {
        // given
        String body = readRequestBody("02-start-update.json");

        // when
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isNull();
        mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        verify(telegramClient, times(1)).sendMessage(any());
    }

    @Test
    @Order(3)
    public void update_03_registeredUserSendsTrackCommand_chatStatusChanges() throws Exception {
        // given
        String body = readRequestBody("03-track-update.json");

        // when
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_URL_TO_TRACK);
        verify(telegramClient, times(1)).sendMessage(any());
    }

    @Test
    @Order(4)
    public void update_04_registeredUserSendsUrlToTrack_chatStatusChanges() throws Exception {
        // given
        Mockito.when(scrapperClient.trackUrl(any())).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));
        String body = readRequestBody("04-url-to-track-update.json");

        // when
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_URL_TO_TRACK);
        mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        verify(scrapperClient, times(1)).trackUrl(any());
        verify(telegramClient, times(1)).sendMessage(any());
    }

    @Test
    @Order(5)
    public void update_05_registeredUserSendsListCommand_userGetsTrackedUrl() throws Exception {
        // given
        Mockito.when(trackingDao.getTrackings(CHAT_ID)).thenReturn(List.of(TRACKED_URL));
        String body = readRequestBody("05-list-update.json");

        // when
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        verify(telegramClient, times(1)).sendMessage(any());

        ArgumentCaptor<SendMessageTo> argument = ArgumentCaptor.forClass(SendMessageTo.class);
        Mockito.verify(telegramClient).sendMessage(argument.capture());
        assertThat(Pattern.matches("^.*%s.*$".formatted(TRACKED_URL), argument.getValue().text())).isTrue();
    }

    @Test
    @Order(6)
    public void update_06_registeredUserSendsUntrackCommand_chatStatusChanges() throws Exception {
        // given
        String body = readRequestBody("06-untrack-update.json");

        // when
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_URL_TO_UNTRACK);
        verify(telegramClient, times(1)).sendMessage(any());
    }

    @Test
    @Order(7)
    public void update_07_registeredUserSendsUrlToUntrack_chatStatusChanges() throws Exception {
        // given
        Mockito.when(scrapperClient.untrackUrl(any())).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));
        String body = readRequestBody("07-url-to-untrack-update.json");

        // when
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_URL_TO_UNTRACK);
        mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        verify(scrapperClient, times(1)).untrackUrl(any());
        verify(telegramClient, times(1)).sendMessage(any());
    }

    @Test
    @Order(8)
    public void update_08_registeredUserSendsListCommand_userGetsEmptyList() throws Exception {
        // given
        Mockito.when(trackingDao.getTrackings(CHAT_ID)).thenReturn(List.of());
        String body = readRequestBody("08-list-update.json");

        // when
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        mockMvc.perform(MockMvcRequestBuilders.post(UPDATE_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        assertThat(chatStatusDao.getChatStatus(CHAT_ID)).isEqualTo(AWAITING_COMMAND);
        verify(telegramClient, times(1)).sendMessage(any());

        ArgumentCaptor<SendMessageTo> argument = ArgumentCaptor.forClass(SendMessageTo.class);
        Mockito.verify(telegramClient).sendMessage(argument.capture());
        assertThat(Pattern.matches("^.*%s.*$".formatted(TRACKED_URL), argument.getValue().text())).isFalse();
    }

    @SneakyThrows
    private String readRequestBody(String fileName) {
        Path path = Path.of("src", "test", "resources", "updates", fileName);
        return Files.readAllLines(path).stream().collect(Collectors.joining("\n"));
    }
}
