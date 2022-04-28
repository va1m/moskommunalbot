package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.State;
import com.va1m.moskommunalbot.interaction.TimeService;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.PriceEntry;
import com.va1m.moskommunalbot.priceproviders.WaterDisposingPricesProvider;
import com.va1m.testools.Json;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

/** Tests {@link ResultsStateProcessor} */
@ExtendWith(MockitoExtension.class)
class ResultsStateProcessorTest {

    @InjectMocks
    private ResultsStateProcessor stateProcessor;

    @Mock
    private ColdWaterPricesProvider coldWater;

    @Mock
    private HotWaterPricesProvider hotWater;

    @Mock
    private WaterDisposingPricesProvider waterDisposing;

    @Mock
    private ElectricityPricesProvider electricity;

    @Mock
    private TimeService timeService;

    @Test
    void forState() {
        assertThat(stateProcessor.forState()).isSameAs(State.SHOWING_RESULTS);
    }

    @Test
    void processOutput() {

        final var interactionContext = Json.deserialize(
            "com/va1m/moskommunalbot/interaction/stateprocessors/interaction-context.json",
            InteractionContext.class);

        final var priceEntry = PriceEntry.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31), 123);
        when(coldWater.provide()).thenReturn(new PriceEntry[] { priceEntry });
        when(hotWater.provide()).thenReturn(new PriceEntry[] { priceEntry });
        when(waterDisposing.provide()).thenReturn(new PriceEntry[] { priceEntry });
        when(electricity.provide()).thenReturn(new PriceEntry[] { priceEntry });
        when(timeService.getToday()).thenReturn(LocalDate.of(2020, 7, 1));

        final var output = stateProcessor.processOutput(interactionContext);

        final var expected = "Стоимость коммунальных услуг:\n\n"
            + "- Холодная вода:\n"
            + "*1.23 руб.* за 1.000 кб.м.\n"
            + "1.23 руб/кб.м. с 01.01.2020\n\n"
            + "- Горячая вода:\n"
            + "*2.46 руб.* за 2.000 кб.м.\n"
            + "1.23 руб/кб.м. с 01.01.2020\n\n"
            + "- Водоотвод (канализация):\n"
            + "*3.69 руб.* за 3.000 кб.м.\n"
            + "1.23 руб/кб.м. с 01.01.2020\n\n"
            + "- Электричество:\n"
            + "*1.23 руб.* за 1.0 КВт\n"
            + "1.23 руб/КВт с 01.01.2020\n\n"
            + "ВСЕГО: *8.61 руб.*\n\n"
            + "Цены взяты из следующих источников:\n"
            + "- [МосВодоКанал](http://www.mosvodokanal.ru/forabonents/tariffs/)\n"
            + "- [МОЭК](https://online.moek.ru/clients/tarify-i-raschety/tarify)\n"
            + "- [МосЭнергоСбыт](https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php)\n\n"
            + "Для начала нового расчета наберите /new.";

        assertThat(output).isEqualTo(expected);
    }
}