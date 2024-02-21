package games.negative.pronouns4j.papi;

import games.negative.pronouns4j.Pronouns4J;
import games.negative.pronouns4j.pronouns.Pronouns;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class PapiExpansion extends PlaceholderExpansion {

    private final Pronouns4J plugin;

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
        if (player == null) {
            plugin.getLogger().log(Level.WARNING, "Failed to parse placeholder because player cannot be null! Using default pronouns (they/them)");
            return "they/them";
        }

        Pronouns pronouns = plugin.getPronounsManager().getPronouns(player.getUniqueId().toString());
        if (pronouns == null) {
            return "they/them"; // Default pronouns
        }

        switch (params.toLowerCase()) {
            case "subjective":
            case "objective":
            case "possessive":
            case "reflexive":
                return getPronounByType(pronouns, params);
            case "pronouns":
                return formatPronouns(pronouns);
            case "subjective_upper":
            case "objective_upper":
            case "possessive_upper":
            case "reflexive_upper":
                return getPronounByType(pronouns, params.substring(0, params.lastIndexOf("_"))).toUpperCase();
            case "pronouns_upper":
                return formatPronouns(pronouns).toUpperCase();
            case "subjective_capital":
            case "objective_capital":
            case "possessive_capital":
            case "reflexive_capital":
                return capitalizeFirstLetter(getPronounByType(pronouns, params.substring(0, params.lastIndexOf("_"))));
            case "pronouns_capital":
                return formatPronounsCapital(pronouns);
            default:
                return null;
        }
    }

    private String getPronounByType(Pronouns pronouns, String type) {
        switch (type) {
            case "subjective":
                return pronouns.getSubjective();
            case "objective":
                return pronouns.getObjective();
            case "possessive":
                return pronouns.getPossessive();
            case "reflexive":
                return pronouns.getReflexive();
            default:
                return null;
        }
    }

    private String formatPronouns(Pronouns pronouns) {
        return pronouns.getSubjective() + "/" + pronouns.getObjective() + "/" + pronouns.getPossessive();
    }

    private String formatPronounsCapital(Pronouns pronouns) {
        return capitalizeFirstLetter(pronouns.getSubjective()) + "/" + capitalizeFirstLetter(pronouns.getObjective()) + "/" + capitalizeFirstLetter(pronouns.getPossessive());
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
