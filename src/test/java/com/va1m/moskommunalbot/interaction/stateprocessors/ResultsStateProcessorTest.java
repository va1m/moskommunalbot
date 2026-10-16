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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
            + "10001.000 - 10000.000 = 1.0кВт·ч\n"
            + "Тариф: 1.23₽/кВт·ч с 01.01.2020\n"
            + "\n"
            + "ВСЕГО: *8.61*₽\n"
            + "\n"
            + "Цены взяты из следующих источников:\n"
            + "- [МосВодоКанал](https://www.mos.ru/upload/documents/docs/4677/Prikazot16122025_DPR-TR-334_25-ghvb2.pdf)\n"
            + "- [МОЭК](https://www.mos.ru/upload/documents/docs/9077/Prikazot15122025_DPR-TR-316_25-ikuj8.pdf)\n"
            + "- [МосЭнергоСбыт](https://www.mos.ru/upload/documents/docs/5117/Prikazot24122025_DPR-TR-407_25-ghvb1.pdf)\n"
            + "\n"
            + "Для начала нового расчета наберите /new.";

        assertThat(output)
            .usingRecursiveComparison()
            .isEqualTo(InteractionMessage.of(expected));
    }

    @ParameterizedTest
    @CsvSource({
        "2026-09-30, 01.01.2026, 66.87, 635.42, 157.44, 7.28, 867.01",
        "2026-10-01, 01.10.2026, 73.79, 755.60, 180.72, 8.46, 1018.57",
        "2026-10-16, 01.10.2026, 73.79, 755.60, 180.72, 8.46, 1018.57",
        "2026-12-31, 01.10.2026, 73.79, 755.60, 180.72, 8.46, 1018.57"
    })
    void shouldCalculateWithApprovedTariffs(LocalDate date, String since, String coldWaterAmount,
        String hotWaterAmount, String wastewaterAmount, String electricityAmount, String totalAmount) {

        final var calculation = Json.deserialize(
            "com/va1m/moskommunalbot/interaction/stateprocessors/interaction-context.json",
            Calculation.class);
        when(timeService.getToday()).thenReturn(date);
        final var processor = new ResultsStateProcessor(new ColdWaterPricesProvider(), new HotWaterPricesProvider(),
            new WaterDisposingPricesProvider(), new ElectricityPricesProvider(), timeService);

        // The fixture consumes 1 m³ cold water, 2 m³ hot water, 3 m³ wastewater and 1 kWh.
        final var text = processor.buildMessageForUser(calculation).getText();

        assertThat(text).contains(
            "- Холодная вода: *" + coldWaterAmount + "*₽",
            "- Горячая вода: *" + hotWaterAmount + "*₽",
            "- Водоотвод (канализация): *" + wastewaterAmount + "*₽",
            "- Электричество: *" + electricityAmount + "*₽",
            "Тариф: " + electricityAmount + "₽/кВт·ч с " + since,
            "ВСЕГО: *" + totalAmount + "*₽");
    }
}
