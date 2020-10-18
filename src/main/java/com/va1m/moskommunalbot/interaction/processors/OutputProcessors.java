package com.va1m.moskommunalbot.interaction.processors;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.State;
import com.va1m.moskommunalbot.priceproviders.PriceEntry;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.WaterDisposingPricesProvider;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Stream;

/** Provides methods (processors) to build output text messages for user */
public final class OutputProcessors {

    private OutputProcessors() {
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_LAST_COLD_WATER_METERS} state */
    public static String lastColdWater(InteractionContext interactionContext) {
        return "Привет!\n" +
                "Начинаем новый расчет за коммунальные услуги.\n" +
                "Пожалуйста, введите показание счетчика холодной воды на начало периода.";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_CURRENT_COLD_WATER_METERS} state */
    public static String currentColdWater(InteractionContext interactionContext) {
        return "Теперь, пожалуйста, введите текущее показание счетчика холодной воды.";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_LAST_HOT_WATER_METERS} state */
    public static String lastHotWater(InteractionContext interactionContext) {
        return "Введите показание счетчика горячей воды на начало периода.";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_CURRENT_HOT_WATER_METERS} state */
    public static String currentHotWater(InteractionContext interactionContext) {
        return "Введите текущее показание счетчика горячей воды.";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_LAST_ELECTRICITY_METERS} state */
    public static String lastElectricity(InteractionContext interactionContext) {
        return "Введите показание счетчика электричества на начало периода.";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_CURRENT_ELECTRICITY_METERS} state */
    public static String currentElectricity(InteractionContext interactionContext) {
        return "Введите текущее показание счетчика электричества.";
    }

    /** Returns text for the NEXT {@link State#SHOWING_RESULTS} state */
    public static String results(InteractionContext interactionContext) {

        final var coldWater = new ColdWaterPricesProvider();
        final var hotWater = new HotWaterPricesProvider();
        final var waterDisposing = new WaterDisposingPricesProvider();
        final var electricity = new ElectricityPricesProvider();

        var template = "* Холодная вода:%n" +
                "%.2fруб. за %dкб.м. (%.2fруб/кб.м. с %s)%n";
        final var consumedColdWater =
                interactionContext.getCurrentColdWaterMeters() - interactionContext.getLatestColdWaterMeters();
        final var coldWaterText = getExpenseEntryText(coldWater.provide(), consumedColdWater, template);

        template = "* Горячая вода:%n" +
                "%.2fруб. за %dкб.м. (%.2fруб/кб.м. с %s)%n";
        final var consumedHotWater =
                interactionContext.getCurrentHotWaterMeters() - interactionContext.getLatestHotWaterMeters();
        final var hotWaterText = getExpenseEntryText(hotWater.provide(), consumedHotWater, template);

        template = "* Водоотвод (канализация):%n" +
                "%.2fруб. за %dкб.м. (%.2fруб/кб.м. с %s)%n";
        final var disposedWater = consumedColdWater + consumedHotWater;
        final var disposedWaterText = getExpenseEntryText(waterDisposing.provide(), disposedWater, template);

        template = "* Электричество:%n" +
                "%.2fруб. за %dКВт (%.2fруб/КВт с %s)%n";
        final var consumedElectricity =
                interactionContext.getCurrentElectricityMeters() - interactionContext.getLatestElectricityMeters();
        final var consumedElectricityText =
                getExpenseEntryText(electricity.provide(), consumedElectricity, template);

        return "Стоимость коммунальных услуг:\n" +
                "\n" +
                coldWaterText +
                hotWaterText +
                disposedWaterText +
                consumedElectricityText +
                "\n" +
                "Цены взяты из следующих источников:\n" +
                "* МосВодоКанал: http://www.mosvodokanal.ru/forabonents/tariffs/\n" +
                "* МОЭК: https://online.moek.ru/clients/tarify-i-raschety/tarify\n" +
                "* МосЭнергоСбыт: https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php\n" +
                "\n" +
                "Для начала нового расчета наберите `/new`.";
    }

    private static String getExpenseEntryText(PriceEntry[] priceEntries, int consumed, String template) {
        final var today = LocalDate.now(ZoneId.of("Europe/Moscow"));

        return Stream.of(priceEntries)
                .filter(price -> today.compareTo(price.getSince()) >= 0)
                .filter(price -> today.compareTo(price.getTill()) <= 0)
                .findFirst()
                .map(price -> {
                    final var dblPrice = ((double) price.getPrice()) / 100.0D;
                    final var totalAmount = ((double) (consumed * price.getPrice())) / 100.0D;
                    return String.format(template,
                            totalAmount, consumed, dblPrice, price.getSince().toString());
                })
                .orElseThrow();
    }
}
