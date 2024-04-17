package edu.bot.service;

import edu.bot.IntegrationTest;
import edu.bot.client.scrapper.ScrapperClient;
import edu.bot.client.telegram.TelegramClient;
import edu.bot.dao.ChatStatusDao;
import edu.bot.dao.TrackingDao;
import edu.bot.dto.request.UpdateTo;
import edu.bot.utils.UpdateToBuilder;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static edu.bot.domain.ChatStatus.AWAITING_COMMAND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UpdatesProcessingServiceTest extends IntegrationTest {

    private static final String START_COMMAND = "/start";
    private static final String TRACK_COMMAND = "/track";
    private static final String UNTRACK_COMMAND = "/untrack";
    private static final String HELP_COMMAND = "/help";

    private static TelegramClient telegramClient = spy(TelegramClient.class);
    private static ScrapperClient scrapperClient = spy(ScrapperClient.class);

    private static ChatStatusDao chatStatusDao = new ChatStatusDao(dataSource);
    private static TrackingDao trackingDao = new TrackingDao(dataSource);
    private static TrackingService trackingService = new TrackingService(
        telegramClient,
        scrapperClient,
        trackingDao
    );
    private static CommandProcessingService commandProcessingService = new CommandProcessingService(
        telegramClient,
        trackingService,
        chatStatusDao
    );
    private static UpdatesProcessingService updatesProcessingService = new UpdatesProcessingService(
        telegramClient,
        commandProcessingService,
        trackingService,
        chatStatusDao
    );

    @Test
    public void processUpdate_startCommandIsSent_NewChatAppearsInDatabase() {
        // given
        Long chatId = 123L;
        UpdateTo updateTo = UpdateToBuilder.builder()
            .setChatId(chatId)
            .setMessage(START_COMMAND)
            .build();

        // when
        assertThat(chatStatusDao.getChatStatus(chatId)).isNull();
        updatesProcessingService.processUpdate(updateTo);

        // then
        assertThat(chatStatusDao.getChatStatus(chatId)).isEqualTo(AWAITING_COMMAND);
        verify(telegramClient, times(1)).sendMessage(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hello, bot!", TRACK_COMMAND, UNTRACK_COMMAND, HELP_COMMAND})
    public void processUpdate_unregisteredUserSendsNotStartCommand_DatabaseDoesNotChange(String message) {
        // given
        Long chatId = 124L;
        UpdateTo updateTo = UpdateToBuilder.builder()
            .setChatId(chatId)
            .setMessage(message)
            .build();

        // when
        assertThat(chatStatusDao.getChatStatus(chatId)).isNull();
        updatesProcessingService.processUpdate(updateTo);

        // then
        assertThat(chatStatusDao.getChatStatus(chatId)).isNull();
    }
}
