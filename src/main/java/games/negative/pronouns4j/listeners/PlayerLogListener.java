package games.negative.pronouns4j.listeners;

import games.negative.pronouns4j.Pronouns4J;
import games.negative.pronouns4j.pronouns.Pronouns;
import games.negative.pronouns4j.pronouns.PronounsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLogListener implements Listener {

    private final Pronouns4J plugin;

    public PlayerLogListener(Pronouns4J plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        PronounsManager pronounsManager = plugin.getPronounsManager();

        // Load pronouns if they exist, otherwise save default pronouns
        Pronouns pronouns = pronounsManager.loadPronouns(uuid);
        if (pronouns == null) {
            pronouns = new Pronouns("they", "them", "their", "themself");
            pronounsManager.savePronouns(uuid, pronouns);
        } else {
            pronounsManager.savePronouns(uuid, pronouns); // Save loaded pronouns again to ensure they're up-to-date
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        plugin.getPronounsManager().savePronounsToStorage(uuid, plugin.getPronounsManager().getPronouns(uuid));
    }
}
