package com.va1m.moskommunalbot;

import com.va1m.moskommunalbot.interaction.InteractionDao;
import com.va1m.moskommunalbot.interaction.InteractionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;

/** Main class */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final InteractionDao INTERACTION_DAO = new InteractionDao();
    private static final InteractionService INTERACTION_SERVICE = new InteractionService(INTERACTION_DAO);

    /** Main method */
    public static void main(String[] args) {

        final var token = Stream.of(args)
                .map(arg -> arg.split("="))
                .filter(nameValPair -> "token".equalsIgnoreCase(nameValPair[0]))
                .findAny()
                .map(pair -> pair[1])
                .orElseThrow();

        ApiContextInitializer.init();
        final var telegramBotsApi = new TelegramBotsApi();
        final var komunalkoBot = new MosKommunalBot(INTERACTION_SERVICE, token);
        try {
            telegramBotsApi.registerBot(komunalkoBot);
        } catch (TelegramApiException e) {
            LOGGER.error("", e);
        }
    }
}
