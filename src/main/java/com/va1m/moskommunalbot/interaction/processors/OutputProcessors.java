package com.va1m.moskommunalbot.interaction.processors;

import com.va1m.moskommunalbot.interaction.InteractionContext;
import com.va1m.moskommunalbot.interaction.State;

public final class OutputProcessors {

    private OutputProcessors() {
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_LAST_COLD_WATER_METERS} state */
    public static String lastColdWater(InteractionContext interactionContext) {
        return "Hello!\n" +
                "Welcome to the new communal expenses calculation.\n" +
                "Please, enter cold water meters from the latest calculation";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_CURRENT_COLD_WATER_METERS} state */
    public static String currentColdWater(InteractionContext interactionContext) {
        return "Now, please, enter current cold water meters";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_LAST_HOT_WATER_METERS} state */
    public static String lastHotWater(InteractionContext interactionContext) {
        return "Enter hot water meters from the latest calculation";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_CURRENT_HOT_WATER_METERS} state */
    public static String currentHotWater(InteractionContext interactionContext) {
        return "Enter current hot water meters";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_LAST_ELECTRICITY_METERS} state */
    public static String lastElectricity(InteractionContext interactionContext) {
        return "Enter the electricity meters from the latest calculation";
    }

    /** Returns text for the NEXT {@link State#WAITING_FOR_CURRENT_ELECTRICITY_METERS} state */
    public static String currentElectricity(InteractionContext interactionContext) {
        return "And now enter current electricity meters";
    }

    /** Returns text for the NEXT {@link State#SHOWING_RESULTS} state */
    public static String results(InteractionContext interactionContext) {
        return "According to the meters you entered the followings communal expenses are calculated:\n" +
                "\n" +
                "Cold water:\n" +
                "\t{} RUB for {} liters ({} RUB/L since {})\n" +
                "Hot water:\n" +
                "\t{} RUB for {} liters ({} RUB/L since {})\n" +
                "Water disposing:\n" +
                "\t{} RUB for {} + {} = {} liters ({} RUB/L since {})\n" +
                "Electricity:\n" +
                "\t{} RUB for {} KW ({} RUB/KW since {})\n" +
                "\n" +
                "Prices are from the following pages:\n" +
                "* Mosvodokanal:http://www.mosvodokanal.ru/forabonents/tariffs/\n" +
                "* MOEK:https://online.moek.ru/clients/tarify-i-raschety/tarify\n" +
                "* Mosenergosbyt:https://www.mosenergosbyt.ru/individuals/tariffs-n-payments/tariffs-msk/kvartiry-i-doma-s-elektricheskimi-plitami.php\n" +
                "\n" +
                "If you want to start a new calculation send me `/new` command.";
    }
}
