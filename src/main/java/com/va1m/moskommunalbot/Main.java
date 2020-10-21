package com.va1m.moskommunalbot;

import com.va1m.moskommunalbot.interaction.InteractionDao;
import com.va1m.moskommunalbot.interaction.InteractionService;
import com.va1m.moskommunalbot.interaction.stateprocessors.*;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.WaterDisposingPricesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

/** Main class */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    // In this project not so many classes to use a dependency injection framework.
    // Let's use for now manual injection instead.
    private static final ColdWaterPricesProvider COLD_WATER_PRICES_PROVIDER = new ColdWaterPricesProvider();
    private static final HotWaterPricesProvider HOT_WATER_PRICES_PROVIDER = new HotWaterPricesProvider();
    private static final WaterDisposingPricesProvider WATER_DISPOSING_PRICES_PROVIDER = new WaterDisposingPricesProvider();
    private static final ElectricityPricesProvider ELECTRICITY_PRICES_PROVIDER = new ElectricityPricesProvider();
    private static final StateProcessor[] STATE_PROCESSORS = registerStateProcessors();
    private static final InteractionDao INTERACTION_DAO = new InteractionDao();
    private static final InteractionService INTERACTION_SERVICE = new InteractionService(INTERACTION_DAO, STATE_PROCESSORS);

    /** Main method */
    public static void main(String[] args) {
        ApiContextInitializer.init();
        final var telegramBotsApi = new TelegramBotsApi();
        final var mosKommunalBot = new MosKommunalBot(INTERACTION_SERVICE);
        try {
            telegramBotsApi.registerBot(mosKommunalBot);
            LOGGER.info("MosKommunalBot is started");
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
        }
    }

    private static StateProcessor[] registerStateProcessors() {
        return new StateProcessor[] {
            new StartStateProcessor(),
            new LastColdWaterMetersStateProcessor(),
            new CurrentColdWaterMetersStateProcessor(),
            new LastHotWaterMetersStateProcessor(),
            new CurrentHotWaterMetersStateProcessor(),
            new LastElectricityMetersStateProcessor(),
            new CurrentElectricityMetersStateProcessor(),
            new ResultsStateProcessor(COLD_WATER_PRICES_PROVIDER, HOT_WATER_PRICES_PROVIDER,
                    WATER_DISPOSING_PRICES_PROVIDER, ELECTRICITY_PRICES_PROVIDER),
        };
    }
}
