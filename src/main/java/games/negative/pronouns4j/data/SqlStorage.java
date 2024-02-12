package games.negative.pronouns4j.data;

import com.google.gson.Gson;
import games.negative.alumina.sql.SQLDatabase;
import games.negative.pronouns4j.pronouns.Pronouns;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlStorage implements DataStorage {

    private final Logger logger = Logger.getLogger(SqlStorage.class.getName());
    private final SQLDatabase database;
    private final Gson gson = new Gson();

    public SqlStorage(SQLDatabase database) {
        logger.info("Loading SQL storage..");
        this.database = database;
        init();
    }

    @Override
    public Pronouns loadPronouns(String uuid) throws SQLException {
        try (PreparedStatement statement = database.connection().prepareStatement("SELECT pronouns FROM pronouns4j WHERE uuid = ?")) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String pronounsJson = resultSet.getString("pronouns");
                    return gson.fromJson(pronounsJson, Pronouns.class);
                } else {
                    logger.log(Level.SEVERE, "No pronouns found for " + uuid);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred while loading pronouns for " + uuid, e);
        }
        return null;
    }

    @Override
    public void savePronouns(String uuid, Pronouns pronouns) {
        String pronounsJson = gson.toJson(pronouns);
        try (PreparedStatement statement = database.connection().prepareStatement("INSERT INTO pronouns4j (uuid, pronouns) VALUES (?, ?) ON DUPLICATE KEY UPDATE pronouns = ?")) {
            statement.setString(1, uuid);
            statement.setString(2, pronounsJson);
            statement.setString(3, pronounsJson);
            statement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving pronouns for " + uuid, e);
        }
    }

    @Override
    public void init() {
        try (PreparedStatement statement = database.connection().prepareStatement("CREATE TABLE IF NOT EXISTS pronouns4j (uuid VARCHAR(36) PRIMARY KEY, pronouns VARCHAR(255))")) {
            statement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error initializing pronouns4j table", e);
        }
    }
}
