package com.va1m.komunalko;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;

/** Main class */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    /** Main method */
    public static void main(String[] args) {

        final var token = Stream.of(args)
                .map(arg -> arg.split("="))
                .filter(nameValPair -> "token".equalsIgnoreCase(nameValPair[0]))
                .findAny()
                .map(pair -> pair[1])
                .orElseThrow();

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new KomunalkoBot(token));
        } catch (TelegramApiException e) {
            LOGGER.error("", e);
        }
    }
}
