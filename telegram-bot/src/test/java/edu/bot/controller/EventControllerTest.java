package edu.bot.controller;

import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.service.EventService;
import edu.bot.utils.transformer.EventTransformer;
import edu.bot.utils.transformer.github.NewIssueEventTransformer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    private static final Long CHAT_ID = 42L;
    private static final String REQUEST_URL = "/%d/sendEvent".formatted(CHAT_ID);
    private static final String CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TelegramClient telegramClient;

    @MethodSource
    private static Stream<Arguments> eventsWithValidEventType() {
        return Stream.of(Arguments.of("01-github-new-issue-event.json"));
    }

    @BeforeEach
    public void resetInvocationsCount(WebApplicationContext webApplicationContext) {
        Mockito.reset(telegramClient);
    }

    @ParameterizedTest
    @MethodSource("eventsWithValidEventType")
    public void sendEventData_eventWithValidEventTypeSent_userReceivesAMessage(String bodyFileName) throws Exception {
        // given
        Mockito.when(telegramClient.sendMessage(any())).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));
        String body = readRequestBody(bodyFileName);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post(REQUEST_URL)
                .contentType(CONTENT_TYPE)
                .content(body))
            .andExpect(status().isOk());

        // then
        ArgumentCaptor<SendMessageTo> argument = ArgumentCaptor.forClass(SendMessageTo.class);
        verify(telegramClient, times(1)).sendMessage(argument.capture());
        assertThat(argument.getValue().chatId()).isEqualTo(CHAT_ID);
    }

    @SneakyThrows
    private String readRequestBody(String fileName) {
        Path path = Path.of("src", "test", "resources", "events", fileName);
        return Files.readAllLines(path).stream().collect(Collectors.joining("\n"));
    }

    @TestConfiguration
    public static class TestConfig {

        public final TelegramClient telegramClient = mock(TelegramClient.class);
        private final List<EventTransformer> eventTransformers = List.of();

        @Bean
        public TelegramClient telegramClient() {
            return mock(TelegramClient.class);
        }

        @Bean
        public List<EventTransformer> eventTransformers() {
            return List.of(new NewIssueEventTransformer());
        }

        @Bean
        public EventService eventService(
            TelegramClient telegramClient,
            List<EventTransformer> eventTransformers
        ) {
            return new EventService(telegramClient, eventTransformers);
        }
    }
}
