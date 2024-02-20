package games.negative.pronouns4j.pronouns;

import games.negative.pronouns4j.data.DataStorage;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

public class PronounsManager {
    private final DataStorage database;
    private final Map<String, Pronouns> pronounsMap;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public PronounsManager(DataStorage database, Map<String, Pronouns> pronounsMap) {
        this.database = database;
        this.pronounsMap = pronounsMap;
        startDatabaseSave();
        Bukkit.getLogger().log(Level.INFO, "PronounsManager has been loaded.");
    }

    private void startDatabaseSave() {
        scheduler.scheduleAtFixedRate(this::databaseSaveTask, 0, 5, java.util.concurrent.TimeUnit.MINUTES);
    }

    public void databaseSaveTask() {
        pronounsMap.forEach(database::savePronouns);
    }

    public void resetPronouns(String uuid) {
        pronounsMap.replace(uuid, new Pronouns("they", "them", "their", "themself"));
        database.savePronouns(uuid, new Pronouns("they", "them", "their", "themself"));
    }

    public Pronouns loadPronouns(String uuid) {
        try {
            Pronouns pronouns = database.loadPronouns(uuid);
            pronounsMap.put(uuid, pronouns);
            return pronouns;
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "An error occurred while loading pronouns for " + uuid);
            e.printStackTrace();
        }
        return null;
    }

    public Pronouns getPronouns(String uuid) {
        return pronounsMap.get(uuid);
    }

    public void savePronouns(String uuid, Pronouns pronouns) {
        if(pronounsMap.containsKey(uuid)) {
            pronounsMap.replace(uuid, pronouns);
        } else {
            pronounsMap.put(uuid, pronouns);
        }
    }

    public void savePronounsToStorage(String uuid, Pronouns pronouns) {
        database.savePronouns(uuid, pronouns);
        ;
    }
}
