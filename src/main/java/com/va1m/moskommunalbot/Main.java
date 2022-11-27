package com.va1m.moskommunalbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/** Main class */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /** Main method */
    public static void main(String[] args) {
        try {
            BotFactory botFactory = DaggerBotFactory.create();

            final var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(botFactory.bot());

            LOGGER.info("MosKommunalBot is started");
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
        }
    }
}
