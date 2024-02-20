package games.negative.pronouns4j.listeners;

import games.negative.pronouns4j.Pronouns4J;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final Pronouns4J plugin;

    public ChatListener(Pronouns4J plugin) {
        this.plugin = plugin;
    }

    // Use of deprecation as new AsyncChatEvent does not have capabilities to change the format
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String format = e.getFormat();

        String subject = plugin.getPronounsManager().getPronouns(p.getUniqueId().toString()).getSubjective();
        String objective = plugin.getPronounsManager().getPronouns(p.getUniqueId().toString()).getObjective();

        e.setFormat(format.replace("<pronouns_sub>", subject).replace("<pronouns_obj>", objective));
    }
}
