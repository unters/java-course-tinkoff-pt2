package edu.bot.utils.command;

import edu.bot.domain.Command;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandParser {

    public static Command resolveCommand(String message) {
        String trimmedMessage = message.trim();
        for (Command command : Command.values()) {
            if (trimmedMessage.equals("/" + command.toString().toLowerCase())) {
                return command;
            }
        }

        return null;
    }
}
