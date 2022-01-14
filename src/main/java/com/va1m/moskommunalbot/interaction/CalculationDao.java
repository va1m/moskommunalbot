package com.va1m.moskommunalbot.interaction;

import static java.util.Optional.ofNullable;

import com.google.inject.Inject;
import com.va1m.moskommunalbot.Settings;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.State;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Provides methods to store and read {@link Calculation} from database */
public class CalculationDao {

    private final Settings.Db dbSettings;

    @Inject
    public CalculationDao(Settings settings) {
        this.dbSettings = settings.getDbSettings();
    }

    /** Reads an {@link Calculation} from database */
    public Optional<Calculation> read(long chatId) {

        try (final var connection = DriverManager.getConnection(dbSettings.getUrl(), dbSettings.getUser(), dbSettings.getPsw())) {

            final var sql = getSelectQuery(chatId);
            try (final var statement = connection.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new Calculation()
                            .setState(State.of(rs.getString("state")))
                            .setLastColdWaterMeters(rs.getObject("last_cold_water_meters", Integer.class))
                            .setCurrentColdWaterMeters(rs.getObject("current_cold_water_meters", Integer.class))
                            .setLastHotWaterMeters(rs.getObject("last_hot_water_meters", Integer.class))
                            .setCurrentHotWaterMeters(rs.getObject("current_hot_water_meters", Integer.class))
                            .setLastElectricityMeters(rs.getObject("last_electricity_meters", Integer.class))
                            .setCurrentElectricityMeters(rs.getObject("current_electricity_meters", Integer.class)));
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return Optional.empty();
    }

    /** Writes an {@link Calculation} to database */
    public void write(long chatId, Calculation calculation) {

        try (final var connection = DriverManager.getConnection(dbSettings.getUrl(), dbSettings.getUser(), dbSettings.getPsw())) {
            try (final var statement = connection.createStatement()) {

                var sql = getUpdateQuery(chatId, calculation);
                var rowsUpdated = statement.executeUpdate(sql);
                if (rowsUpdated == 0) {
                    sql = getInsertQuery(chatId, calculation);
                    rowsUpdated = statement.executeUpdate(sql);
                    if (rowsUpdated == 0) {
                        throw new IllegalStateException("Row is neither inserted nor updated");
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String getSelectQuery(long chatId) {
        return String.format("select * from calculation where chat_id=%d;", chatId);
    }

    private static String getInsertQuery(long chatId, Calculation calculation) {
        return String.format("insert into calculation ("
                + "chat_id, state, last_cold_water_meters, current_cold_water_meters, "
                + "last_hot_water_meters, current_hot_water_meters, last_electricity_meters, "
                + "current_electricity_meters) "
                + "values (%d, '%s', %s, %s, %s, %s, %s, %s);",
            chatId,
            ofNullable(calculation.getState()).map(Enum::name).orElse("null"),
            calculation.getLastColdWaterMeters(),
            calculation.getCurrentColdWaterMeters(),
            calculation.getLastHotWaterMeters(),
            calculation.getCurrentHotWaterMeters(),
            calculation.getLastElectricityMeters(),
            calculation.getCurrentElectricityMeters());
    }

    private static String getUpdateQuery(long chatId, Calculation calculation) {
        return String.format("update calculation "
                + "set state='%s', last_cold_water_meters=%s, current_cold_water_meters=%s,"
                + "    last_hot_water_meters=%s, current_hot_water_meters=%s, last_electricity_meters=%s,"
                + "    current_electricity_meters=%s "
                + "where chat_id=%d;",
            ofNullable(calculation.getState()).map(Enum::name).orElse("null"),
            calculation.getLastColdWaterMeters(),
            calculation.getCurrentColdWaterMeters(),
            calculation.getLastHotWaterMeters(),
            calculation.getCurrentHotWaterMeters(),
            calculation.getLastElectricityMeters(),
            calculation.getCurrentElectricityMeters(),
            chatId);
    }
}
