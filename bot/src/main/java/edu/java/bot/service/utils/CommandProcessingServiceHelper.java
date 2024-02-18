package edu.java.bot.service.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandProcessingServiceHelper {

    private static final String WRONG_COMMAND_MESSAGE = """
            Sorry, but given command is wrong. Type `/help` to see full list of commands.
            """;

    private static final String HELP_MESSAGE = """
            List of available commands:
            - /help             Print this message.
            - /track <url>      Track given url.
            - /untrack <url>    Stop tracking given url.
            - /list             Get urls being tracked.
            """;

    public static String getWrongCommandMessage() {
        return WRONG_COMMAND_MESSAGE;
    }

    public static String getHelpMessage() {
        return HELP_MESSAGE;
    }
}
