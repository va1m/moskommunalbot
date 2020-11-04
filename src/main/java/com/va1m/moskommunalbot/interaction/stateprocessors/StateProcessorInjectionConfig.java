package com.va1m.moskommunalbot.interaction.stateprocessors;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.va1m.moskommunalbot.interaction.InteractionService;

/**
 * Configures guice to inject multiple implementations {@link StateProcessor}
 * @see InteractionService#InteractionService
 */
public class StateProcessorInjectionConfig extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<StateProcessor> multibinder = Multibinder.newSetBinder(binder(), StateProcessor.class);
        multibinder.addBinding().to(CurrentColdWaterMetersStateProcessor.class);
        multibinder.addBinding().to(CurrentElectricityMetersStateProcessor.class);
        multibinder.addBinding().to(CurrentHotWaterMetersStateProcessor.class);
        multibinder.addBinding().to(LastColdWaterMetersStateProcessor.class);
        multibinder.addBinding().to(LastElectricityMetersStateProcessor.class);
        multibinder.addBinding().to(LastHotWaterMetersStateProcessor.class);
        multibinder.addBinding().to(ResultsStateProcessor.class);
        multibinder.addBinding().to(StartStateProcessor.class);
        multibinder.addBinding().to(UseLastMetersStateProcessor.class);
    }
}
