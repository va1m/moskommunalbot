package com.va1m.moskommunalbot;

import com.va1m.moskommunalbot.interaction.InteractionService;
import com.va1m.moskommunalbot.interaction.TimeService;
import com.va1m.moskommunalbot.interaction.stateprocessors.CurrentColdWaterMetersStateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.CurrentElectricityMetersStateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.CurrentHotWaterMetersStateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.LastColdWaterMetersStateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.LastElectricityMetersStateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.LastHotWaterMetersStateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.ResultsStateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.StartStateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.StateProcessor;
import com.va1m.moskommunalbot.interaction.stateprocessors.UseLastMetersStateProcessor;
import com.va1m.moskommunalbot.priceproviders.ColdWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.ElectricityPricesProvider;
import com.va1m.moskommunalbot.priceproviders.HotWaterPricesProvider;
import com.va1m.moskommunalbot.priceproviders.WaterDisposingPricesProvider;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.IntoSet;

/**
 * Configures dagger to inject multiple implementations of {@link StateProcessor}
 *
 * @see InteractionService#InteractionService
 */
@Module
public class StateProcessorsModule {

    private StateProcessorsModule() {
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor currentColdWaterMetersStateProcessorProvider(){
        return new CurrentColdWaterMetersStateProcessor();
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor currentElectricityMetersStateProcessorProvider(){
        return new CurrentElectricityMetersStateProcessor();
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor currentHotWaterMetersStateProcessorProvider(){
        return new CurrentHotWaterMetersStateProcessor();
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor lastColdWaterMetersStateProcessorProvider(){
        return new LastColdWaterMetersStateProcessor();
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor lastElectricityMetersStateProcessorProvider(){
        return new LastElectricityMetersStateProcessor();
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor lastHotWaterMetersStateProcessorProvider(){
        return new LastHotWaterMetersStateProcessor();
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor resultsStateProcessorProvider(ColdWaterPricesProvider coldWater, HotWaterPricesProvider hotWater,
        WaterDisposingPricesProvider waterDisposing, ElectricityPricesProvider electricity, TimeService timeService){

        return new ResultsStateProcessor(coldWater, hotWater, waterDisposing, electricity, timeService);
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor startStateProcessorProvider(){
        return new StartStateProcessor();
    }

    @Provides
    @IntoSet
    @Reusable
    static StateProcessor useLastMetersStateProcessorProvider(){
        return new UseLastMetersStateProcessor();
    }
}
