package com.va1m.moskommunalbot;

import com.va1m.moskommunalbot.interaction.InteractionDao;
import com.va1m.moskommunalbot.interaction.InteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

/** Main class */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final InteractionDao INTERACTION_DAO = new InteractionDao();
    private static final InteractionService INTERACTION_SERVICE = new InteractionService(INTERACTION_DAO);

    /** Main method */
    public static void main(String[] args) {

        ApiContextInitializer.init();
        final var telegramBotsApi = new TelegramBotsApi();
        final var mosKommunalBot = new MosKommunalBot(INTERACTION_SERVICE);
        try {
            telegramBotsApi.registerBot(mosKommunalBot);
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
        }
    }
}
