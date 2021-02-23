package com.va1m.moskommunalbot;

import com.google.inject.Guice;
import com.va1m.moskommunalbot.interaction.stateprocessors.StateProcessorInjectionConfig;
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
            final var injector = Guice.createInjector(new StateProcessorInjectionConfig());
            final var mosKommunalBot = injector.getInstance(MosKommunalBot.class);

            final var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(mosKommunalBot);

            LOGGER.info("MosKommunalBot is started");
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
        }
    }
}
