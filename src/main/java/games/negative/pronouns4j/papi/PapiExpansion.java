package games.negative.pronouns4j.papi;

import games.negative.pronouns4j.Pronouns4J;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PapiExpansion extends PlaceholderExpansion {

    private final Pronouns4J plugin; //

    public PapiExpansion(Pronouns4J plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "pronouns4j";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        switch (params) {
            case "subjective":
                return plugin.getPronounsManager().getPronouns(player.getUniqueId().toString()).getSubjective();
            case "objective":
                return plugin.getPronounsManager().getPronouns(player.getUniqueId().toString()).getObjective();
            case "possessive":
                return plugin.getPronounsManager().getPronouns(player.getUniqueId().toString()).getPossessive();
            case "reflexive":
                return plugin.getPronounsManager().getPronouns(player.getUniqueId().toString()).getReflexive();

        }

        return null;
    }
}
