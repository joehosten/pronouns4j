package games.negative.pronouns4j.papi;

import games.negative.pronouns4j.Pronouns4J;
import games.negative.pronouns4j.pronouns.Pronouns;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

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
        return "Pronouns4j";
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
            Bukkit.getLogger().log(Level.WARNING, "Failed to parse placeholder because player cannot be null! Using default pronouns (they/them)");

            return "they/them";
        }

        Pronouns pronouns = plugin.getPronounsManager().getPronouns(player.getUniqueId().toString());
        switch (params) {
            // standard placeholders
            case "subjective":
                return pronouns.getSubjective();
            case "objective":
                return pronouns.getObjective();
            case "possessive":
                return pronouns.getPossessive();
            case "reflexive":
                return pronouns.getReflexive();
            case "pronouns":
                return pronouns.getSubjective() + "/" + pronouns.getObjective() + "/" + pronouns.getPossessive();
            // uppercase
            case "subjective_upper":
                return toUpperCase(pronouns.getSubjective());
            case "objective_upper":
                return toUpperCase(pronouns.getObjective());
            case "possessive_upper":
                return toUpperCase(pronouns.getPossessive());
            case "reflexive_upper":
                return toUpperCase(pronouns.getReflexive());
            case "pronouns_upper":
                return toUpperCase(pronouns.getSubjective()) + "/" + toUpperCase(pronouns.getObjective()) + "/" + toUpperCase(pronouns.getPossessive());
            // sentence case
            case "subjective_capital":
                return capitalizeFirstLetter(pronouns.getSubjective());
            case "objective_capital":
                return capitalizeFirstLetter(pronouns.getObjective());
            case "possessive_capital":
                return capitalizeFirstLetter(pronouns.getPossessive());
            case "reflexive_capital":
                return capitalizeFirstLetter(pronouns.getReflexive());
            case "pronouns_capital":
                return capitalizeFirstLetter(pronouns.getSubjective()) + "/" + capitalizeFirstLetter(pronouns.getObjective()) + "/" + capitalizeFirstLetter(pronouns.getPossessive());
        }
        return null;
    }

    private String toUpperCase(String str) {
        return str.toUpperCase();
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
