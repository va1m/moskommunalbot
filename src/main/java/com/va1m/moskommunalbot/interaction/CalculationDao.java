package com.va1m.moskommunalbot.interaction;

import static java.util.Optional.ofNullable;

import com.va1m.moskommunalbot.DataSource;
import com.va1m.moskommunalbot.model.Calculation;
import com.va1m.moskommunalbot.model.State;
import dagger.Reusable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.inject.Inject;

/** Provides methods to store and read {@link Calculation} from database */
@Reusable
public class CalculationDao {

    private final DataSource dataSource;

    @Inject
    public CalculationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Reads an {@link Calculation} from database */
    public Optional<Calculation> read(long chatId) {

        try (final var connection = dataSource.getConnection()) {

            final var sql = "select * from calculation where chat_id=?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, chatId);

                try (ResultSet rs = statement.executeQuery()) {

                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    return Optional.of(new Calculation()
                        .setState(State.of(rs.getString("state")))
                        .setLastColdWaterMeters((Integer) rs.getObject("last_cold_water_meters"))
                        .setCurrentColdWaterMeters((Integer) rs.getObject("current_cold_water_meters"))
                        .setLastHotWaterMeters((Integer) rs.getObject("last_hot_water_meters"))
                        .setCurrentHotWaterMeters((Integer) rs.getObject("current_hot_water_meters"))
                        .setLastElectricityMeters((Integer) rs.getObject("last_electricity_meters"))
                        .setCurrentElectricityMeters((Integer) rs.getObject("current_electricity_meters"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Writes an {@link Calculation} to database */
    public void write(long chatId, Calculation calculation) {

        try (final var connection = dataSource.getConnection()) {
            int rowsUpdated = updateCalculation(connection, chatId, calculation);
            if (rowsUpdated == 0) {
                rowsUpdated = insertCalculation(connection, chatId, calculation);
                if (rowsUpdated == 0) {
                    throw new IllegalStateException("Row is neither inserted nor updated");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private int updateCalculation(Connection cnn, long chatId, Calculation calculation) throws SQLException {
        var sql = """
            update calculation
            set state=?, last_cold_water_meters=?, current_cold_water_meters=?,
                last_hot_water_meters=?, current_hot_water_meters=?, last_electricity_meters=?,
                current_electricity_meters=?
            where chat_id=?
            """;
        try (final var statement = cnn.prepareStatement(sql)) {

            statement.setString(1, ofNullable(calculation.getState()).map(Enum::name).orElse(null));
            setInt(statement, 2, calculation.getLastColdWaterMeters());
            setInt(statement, 3, calculation.getCurrentColdWaterMeters());
            setInt(statement, 4, calculation.getLastHotWaterMeters());
            setInt(statement, 5, calculation.getCurrentHotWaterMeters());
            setInt(statement, 6, calculation.getLastElectricityMeters());
            setInt(statement, 7, calculation.getCurrentElectricityMeters());
            statement.setLong(8, chatId);

            return statement.executeUpdate();
        }
    }

    private int insertCalculation(Connection cnn, long chatId, Calculation calculation) throws SQLException {
        String sql = """
            insert into calculation (
                chat_id, state, last_cold_water_meters, current_cold_water_meters,
                last_hot_water_meters, current_hot_water_meters, last_electricity_meters,
                current_electricity_meters)
            values (?, ?, ?, ?, ?, ?, ?, ?);
            """;
        try (final var statement = cnn.prepareStatement(sql)) {

            statement.setLong(1, chatId);
            statement.setString(2, ofNullable(calculation.getState()).map(Enum::name).orElse(null));
            setInt(statement, 3, calculation.getLastColdWaterMeters());
            setInt(statement, 4, calculation.getCurrentColdWaterMeters());
            setInt(statement, 5, calculation.getLastHotWaterMeters());
            setInt(statement, 6, calculation.getCurrentHotWaterMeters());
            setInt(statement, 7, calculation.getLastElectricityMeters());
            setInt(statement, 8, calculation.getCurrentElectricityMeters());

            return statement.executeUpdate();
        }
    }

    public void setInt(PreparedStatement statement, int idx, Integer value) throws SQLException {
        if (value != null) {
            statement.setInt(idx, value);
        }
    }
}
