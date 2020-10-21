package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.State;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.PriceEntry;
import com.va1m.moskommunalbot.priceproviders.WaterDisposingPricesProvider;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Stream;

/** Prepare output message text for {@link State#SHOWING_RESULTS} state and then handles user input data */
public class ResultsStateProcessor implements StateProcessor {

    final ColdWaterPricesProvider coldWater;
    final HotWaterPricesProvider hotWater;
    final WaterDisposingPricesProvider waterDisposing;
    final ElectricityPricesProvider electricity;

    /** Constructor */
    public ResultsStateProcessor(ColdWaterPricesProvider coldWater, HotWaterPricesProvider hotWater,
        WaterDisposingPricesProvider waterDisposing, ElectricityPricesProvider electricity) {

        this.coldWater = coldWater;
        this.hotWater = hotWater;
        this.waterDisposing = waterDisposing;
        this.electricity = electricity;
    }

    @Override
    public State forState() {
        return State.SHOWING_RESULTS;
    }

    @Override
    public void processInput(String input, InteractionContext interactionContext) {
        // Nothing to do here
    }

    @Override
    public String processOutput(InteractionContext interactionContext) {

        var template = "- Холодная вода:%n" +
                "%.2fруб. за %dкб.м.%n" +
                "(%.2fруб/кб.м. с %s)%n%n";
        final var consumedColdWater =
                interactionContext.getCurrentColdWaterMeters() - interactionContext.getLatestColdWaterMeters();
        final var coldWaterText = getExpenseEntryText(coldWater.provide(), consumedColdWater, template);

        template = "- Горячая вода:%n" +
                "%.2fруб. за %dкб.м.%n" +
                "(%.2fруб/кб.м. с %s)%n%n";
        final var consumedHotWater =
                interactionContext.getCurrentHotWaterMeters() - interactionContext.getLatestHotWaterMeters();
        final var hotWaterText = getExpenseEntryText(hotWater.provide(), consumedHotWater, template);

        template = "- Водоотвод (канализация):%n" +
                "%.2fруб. за %dкб.м.%n" +
                "(%.2fруб/кб.м. с %s)%n%n";
        final var disposedWater = consumedColdWater + consumedHotWater;
        final var disposedWaterText = getExpenseEntryText(waterDisposing.provide(), disposedWater, template);

        template = "- Электричество:%n" +
                "%.2fруб. за %dКВт%n" +
                "(%.2fруб/КВт с %s)%n";
        final var consumedElectricity =
                interactionContext.getCurrentElectricityMeters() - interactionContext.getLatestElectricityMeters();
        final var consumedElectricityText =
                getExpenseEntryText(electricity.provide(), consumedElectricity, template);

        return "Стоимость коммунальных услуг:\n"
                + "\n"
                + coldWaterText
                + hotWaterText
                + disposedWaterText
                + consumedElectricityText
                + "\n"
                + "Цены взяты из следующих источников:\n"
                + "- [МосВодоКанал](http://www.mosvodokanal.ru/forabonents/tariffs/)\n"
                + "- [МОЭК](https://online.moek.ru/clients/tarify-i-raschety/tarify)\n"
                + "- [МосЭнергоСбыт](https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php)\n"
                + "\n"
                + "Для начала нового расчета наберите /new.";
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
