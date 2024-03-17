package edu.bot.utils.command;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandProcessingHelper {

    private static final String WRONG_COMMAND_MESSAGE = """
        Sorry, but given command is wrong. Type `/help` to see full list of commands.
        """;
    private static final String HELP_MESSAGE = """
        List of available commands:
        - /start : Print welcome message.
        - /help : Print this message.
        - /track : Track given url.
        - /untrack : Stop tracking given url.
        - /list : Get urls being tracked.
        """;

    public static String getWrongCommandMessage() {
        return WRONG_COMMAND_MESSAGE;
    }

    public static String getHelpMessage() {
        return HELP_MESSAGE;
    }
}
