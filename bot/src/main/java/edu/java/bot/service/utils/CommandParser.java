package edu.java.bot.service.utils;

import edu.java.bot.domain.Command;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandParser {

    public static Command resolveCommand(@NotNull String message) {
        String trimmedMessage = message.trim();
        for (Command command : Command.values()) {
            if (trimmedMessage.equals("/" + command.toString().toLowerCase())) {
                return command;
            }
        }

        return null;
    }
}
