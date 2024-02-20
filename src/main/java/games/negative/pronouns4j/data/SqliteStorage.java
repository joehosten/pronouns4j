package games.negative.pronouns4j.data;

import com.google.gson.Gson;
import games.negative.alumina.sql.impl.SQLiteDatabase;
import games.negative.pronouns4j.pronouns.Pronouns;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteStorage implements DataStorage {

    private final Logger logger = Logger.getLogger(SqliteStorage.class.getName());
    private final SQLiteDatabase database;
    private final Gson gson = new Gson();

    public SqliteStorage(SQLiteDatabase database) {
        logger.info("Loading SQLite storage..");
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
        try (PreparedStatement statement = database.connection().prepareStatement("INSERT OR REPLACE INTO pronouns4j (uuid, pronouns) VALUES (?, ?)")) {
            statement.setString(1, uuid);
            statement.setString(2, pronounsJson);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving pronouns for " + uuid, e);
        }
    }

    @Override
    public void init() {
        try (Statement statement = database.connection().createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pronouns4j (uuid TEXT PRIMARY KEY, pronouns TEXT)");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error initializing pronouns4j table", e);
        }
    }

    // Close the connection
    public void close() {
        try {
            if (database.connection() != null) {
                database.connection().close();
                logger.info("SQLite connection closed.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error closing SQLite connection", e);
        }
    }
}
