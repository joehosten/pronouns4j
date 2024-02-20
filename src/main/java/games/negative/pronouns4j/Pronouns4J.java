package games.negative.pronouns4j;

import games.negative.alumina.AluminaPlugin;
import games.negative.alumina.sql.SQLCredentials;
import games.negative.alumina.sql.SQLDatabase;
import games.negative.alumina.sql.SQLDatabases;
import games.negative.alumina.sql.impl.SQLiteDatabase;
import games.negative.pronouns4j.commands.CommandPronouns;
import games.negative.pronouns4j.data.DataStorage;
import games.negative.pronouns4j.data.SqlStorage;
import games.negative.pronouns4j.data.SqliteStorage;
import games.negative.pronouns4j.listeners.ChatListener;
import games.negative.pronouns4j.listeners.PlayerLogListener;
import games.negative.pronouns4j.metrics.Metrics;
import games.negative.pronouns4j.papi.PapiExpansion;
import games.negative.pronouns4j.pronouns.Pronouns;
import games.negative.pronouns4j.pronouns.PronounsManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

public final class Pronouns4J extends AluminaPlugin {

    @Getter
    private static Pronouns4J instance;
    private final HashMap<String, Pronouns> cache = new HashMap<>();
    @Getter
    private PronounsManager pronounsManager;
    private DataStorage dataStorage;
    private SQLDatabase database;

    @Override
    public void load() {
    }

    @Override
    @SneakyThrows
    public void enable() {
        // Load prequisites
        instance = this;
        saveDefaultConfig();
        getLogger().log(Level.INFO, "Pronouns4J loading...");

        // Managers
        initializeDataStorage();
        this.pronounsManager = new PronounsManager(dataStorage, cache);
        Locale.init(this);
        registerCommands();

        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PapiExpansion(this).register();
            getLogger().log(Level.INFO, "PlaceholderAPI found! Hooking into it.");
        } else {
            getLogger().log(Level.WARNING, "PlaceholderAPI not found! Placeholder expansion will not be registered.");
        }

        // Finish loading
        getLogger().log(Level.INFO, "Pronouns4J has been loaded.");
        new Metrics(this, 20982);

        if(Bukkit.getPluginManager().isPluginEnabled("EssentialsChat")) {
            getLogger().log(Level.INFO, "EssentialsChat found! Hooking into it.");
            registerListeners(new ChatListener(this));
        }
        registerListeners(new PlayerLogListener(this));
    }

    private void initializeDataStorage() throws SQLException, IOException, ClassNotFoundException {
        boolean sql = getConfig().getBoolean("sql.enabled");
        if (sql) {
            SQLDatabase database = (SQLDatabase) initializeDatabase(true);
            this.dataStorage = new SqlStorage(database);
            this.database = database;
        } else {
            SQLiteDatabase database = (SQLiteDatabase) initializeDatabase(false);
            this.dataStorage = new SqliteStorage(database);
            this.database = database;
        }
    }

    private Object initializeDatabase(boolean sql) throws SQLException, IOException, ClassNotFoundException {
        FileConfiguration config = getConfig();
        if (sql) {
            return SQLDatabases.mysql(new SQLCredentials(config.getString("sql.host"), config.getInt("sql.port"), config.getString("sql.database"), config.getString("sql.username"), config.getString("sql.password")));
        } else {
            createSQLiteDatabase();
            File file = new File(getDataFolder(), "database.db");
            return SQLDatabases.lite(file);
        }
    }

    private void createSQLiteDatabase() {
        // Get the data folder of the plugin
        File dataFolder = getDataFolder();

        // Ensure the data folder exists
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        // Construct the path to the SQLite database file
        String databasePath = dataFolder.getAbsolutePath() + File.separator + "database.db";
        Path path = Paths.get(databasePath);

        // Check if the database file exists
        if (!Files.exists(path)) {
            // If the file doesn't exist, create a new one
            try {
                Files.createFile(path);
                getLogger().info("SQLite database file created successfully.");
            } catch (IOException e) {
                getLogger().severe("Error creating SQLite database file: " + e.getMessage());
            }
        }
    }

    private void registerCommands() {
        registerCommand(new CommandPronouns());
    }



    @SneakyThrows
    @Override
    public void disable() {
        pronounsManager.databaseSaveTask();
        database.disconnect();
    }
}
