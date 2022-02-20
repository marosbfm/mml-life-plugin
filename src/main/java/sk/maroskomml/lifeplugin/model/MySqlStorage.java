package sk.maroskomml.lifeplugin.model;

import sk.maroskomml.lifeplugin.Config;
import sk.maroskomml.lifeplugin.PlayerEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySqlStorage implements Storage {

    private static final String GET_PLAYER_BY_MC_ID = "SELECT " +
            "mcId, nick, lifeCount, score, deaths, killedPlayers, killedMobs, killedHostileMobs, gainedXP, spendTimeOnServer, lastLoginTs, createTs, updateTs " +
            "FROM Player WHERE mcId = ?";
    private static final String GET_TOP_PLAYERS = "SELECT " +
            "mcId, nick, lifeCount, score, deaths, killedPlayers, killedMobs, killedHostileMobs, gainedXP, spendTimeOnServer, lastLoginTs, createTs, updateTs " +
            "FROM Player ORDER BY %s DESC LIMIT %d";


    private static final String INSERT_UPDATE_PLAYER_SQL = "INSERT INTO Player " +
            "(mcId, nick, lifeCount, score, deaths, killedPlayers, killedMobs, " +
            "killedHostileMobs, gainedXP, spendTimeOnServer, lastLoginTs, createTs) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
            "ON DUPLICATE KEY UPDATE " +
            "nick = ?, lifeCount = ?, score = ?, deaths = ?, killedPlayers = ?, killedMobs = ?, " +
            "killedHostileMobs = ?, gainedXP = ?, spendTimeOnServer = ?, lastLoginTs = ?, createTs = ?";

    public MySqlStorage(Config config) {
    }

    @Override
    public void store(Map<String, PlayerEntity> players) {
        players.values().forEach(this::insertUpdatePlayer);
    }

    private void insertUpdatePlayer(PlayerEntity playerEntity) {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_UPDATE_PLAYER_SQL)) {
            int i = 1;
            statement.setString(i++, playerEntity.getMcId());
            statement.setString(i++, playerEntity.getNick());
            statement.setInt(i++, playerEntity.getLifeCount());
            statement.setInt(i++, playerEntity.getScore());
            statement.setInt(i++, playerEntity.getDeaths());
            statement.setInt(i++, playerEntity.getKilledPlayers());
            statement.setInt(i++, playerEntity.getKilledMobs());
            statement.setInt(i++, playerEntity.getKilledHostileMobs());
            statement.setInt(i++, playerEntity.getGainedXP());
            statement.setLong(i++, playerEntity.getSpendTimeOnServer());
            statement.setTimestamp(i++, Timestamp.from(Instant.ofEpochMilli(playerEntity.getLastLoginTs())));
            statement.setTimestamp(i++, Timestamp.from(Instant.ofEpochMilli(playerEntity.getCreateTs())));

            statement.setString(i++, playerEntity.getNick());
            statement.setInt(i++, playerEntity.getLifeCount());
            statement.setInt(i++, playerEntity.getScore());
            statement.setInt(i++, playerEntity.getDeaths());
            statement.setInt(i++, playerEntity.getKilledPlayers());
            statement.setInt(i++, playerEntity.getKilledMobs());
            statement.setInt(i++, playerEntity.getKilledHostileMobs());
            statement.setInt(i++, playerEntity.getGainedXP());
            statement.setLong(i++, playerEntity.getSpendTimeOnServer());
            statement.setTimestamp(i++, Timestamp.from(Instant.ofEpochMilli(playerEntity.getLastLoginTs())));
            statement.setTimestamp(i++, Timestamp.from(Instant.ofEpochMilli(playerEntity.getCreateTs())));

            statement.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public PlayerEntity getPlayer(String playerMcId) {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PLAYER_BY_MC_ID);
        ) {
            statement.setString(1, playerMcId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new PlayerEntity(
                            resultSet.getString("mcId"),
                            resultSet.getString("nick"),
                            resultSet.getInt("lifeCount"),
                            resultSet.getInt("score"),
                            resultSet.getInt("deaths"),
                            resultSet.getInt("killedPlayers"),
                            resultSet.getInt("killedMobs"),
                            resultSet.getInt("killedHostileMobs"),
                            resultSet.getInt("gainedXP"),
                            resultSet.getLong("spendTimeOnServer"),
                            resultSet.getTimestamp("lastLoginTs").getTime(),
                            resultSet.getTimestamp("createTs").getTime()
                    );
                }
                return null;
            }
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<PlayerEntity> getTopPlayers(int top, TopCategory topCategory) {
        List<PlayerEntity> result = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     String.format(GET_TOP_PLAYERS, topCategory.getCategoryName(), top)
             )
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new PlayerEntity(
                            resultSet.getString("mcId"),
                            resultSet.getString("nick"),
                            resultSet.getInt("lifeCount"),
                            resultSet.getInt("score"),
                            resultSet.getInt("deaths"),
                            resultSet.getInt("killedPlayers"),
                            resultSet.getInt("killedMobs"),
                            resultSet.getInt("killedHostileMobs"),
                            resultSet.getInt("gainedXP"),
                            resultSet.getLong("spendTimeOnServer"),
                            resultSet.getTimestamp("lastLoginTs").getTime(),
                            resultSet.getTimestamp("createTs").getTime()
                    ));
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }
}
