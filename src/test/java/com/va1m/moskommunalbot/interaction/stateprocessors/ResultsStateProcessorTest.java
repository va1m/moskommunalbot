package com.va1m.moskommunalbot.interaction.stateprocessors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.va1m.moskommunalbot.model.State;
import com.va1m.moskommunalbot.interaction.TimeService;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.InteractionMessage;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.model.Price;
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
            Calculation.class);

        final var price = Price.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31), 123);
        when(coldWater.provide()).thenReturn(new Price[] { price });
        when(hotWater.provide()).thenReturn(new Price[] { price });
        when(waterDisposing.provide()).thenReturn(new Price[] { price });
        when(electricity.provide()).thenReturn(new Price[] { price });
        when(timeService.getToday()).thenReturn(LocalDate.of(2020, 7, 1));

        final var output = stateProcessor.buildMessageForUser(interactionContext);

        final var expected = "Стоимость коммунальных услуг:\n"
            + "\n"
            + "- Холодная вода: *1.23*₽\n"
            + "101.000 - 100.000 = 1.000м³\n"
            + "Тариф: 1.23₽/м³ с 01.01.2020\n"
            + "\n"
            + "- Горячая вода: *2.46*₽\n"
            + "202.000 - 200.000 = 2.000м³\n"
            + "Тариф: 1.23₽/м³ с 01.01.2020\n"
            + "\n"
            + "- Водоотвод (канализация): *3.69*₽\n"
            + "1.000 + 2.000 = 3.000м³\n"
            + "Тариф: 1.23₽/м³ с 01.01.2020\n"
            + "\n"
            + "- Электричество: *1.23*₽\n"
            + "10001.000 - 10000.000 = 1.0КВт\n"
            + "Тариф: 1.23₽/КВт с 01.01.2020\n"
            + "\n"
            + "ВСЕГО: *8.61*₽\n"
            + "\n"
            + "Цены взяты из следующих источников:\n"
            + "- [МосВодоКанал](http://www.mosvodokanal.ru/forabonents/tariffs/)\n"
            + "- [МОЭК](https://online.moek.ru/clients/tarify-i-raschety/tarify)\n"
            + "- [МосЭнергоСбыт](https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php)\n"
            + "\n"
            + "Для начала нового расчета наберите /new.";

        assertThat(output)
            .usingRecursiveComparison()
            .isEqualTo(InteractionMessage.of(expected));
    }
}