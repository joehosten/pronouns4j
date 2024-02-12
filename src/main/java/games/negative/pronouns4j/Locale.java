package games.negative.pronouns4j;

import games.negative.alumina.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public enum Locale {

    PLAYER_NOT_FOUND("&cPlayer not found!"),
    TITLE_HELP(
            "&8[ &3Pronouns4&bJ &8]",
            "&e/pronouns &7- &fDisplays this help menu",
            "&e/pronouns set &7- &fSet your pronouns",
            "&e/pronouns view &7- &fSee what your pronouns look like",
            "&e/pronouns clear &7- &fClears your pronouns to the default (they/them)"
    ),

    CLEAR_SELF_PRONOUNS("&aYour pronouns have been cleared!", "&7They have been reset to the default (they/them)!"),

    CLEAR_OTHER_PRONOUNS("&a%player%'s pronouns have been cleared!"),

    SET_PRONOUNS("&aYour pronouns have been set to %pronouns%!", "&7Use &e/pronouns view &7to see what they look like!"),

    VIEW_PRONOUNS("&aYour pronouns are currently set to %pronouns%!",
            "&7Examples:",
            "&7- &aSubjective: &e'%sub% is amazing!'",
            "&7- &aObjective: &e'I love %obj%!'",
            "&7- &aPossessive: &e'%pos% cat is cute.'",
            "&7- &aReflexive: &e'The person loves %ref%'",
            "&7Use &e/pronouns set &7to change them!"),


    ;
    private final String[] defMessage;
    private Message message;

    Locale(String... defMessage) {
        this.defMessage = defMessage;
    }

    public static void init(@NotNull Pronouns4J plugin) throws IOException {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        validateFile(plugin, file);

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        boolean changed = false;
        for (Locale entry : values()) {
            if (config.isSet(entry.name())) continue;

            List<String> message = List.of(entry.defMessage);
            config.set(entry.name(), message);
            changed = true;
        }

        if (changed) saveFile(plugin, file, config);

        for (Locale entry : values()) {
            entry.message = Message.of(config.getStringList(entry.name()));
        }
    }

    private static void saveFile(@NotNull Pronouns4J plugin, @NotNull File file, @NotNull FileConfiguration config) throws IOException {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages.yml file!");
        }
    }

    private static void validateFile(@NotNull Pronouns4J plugin, @NotNull File file) {
        if (!file.exists()) {
            boolean dirSuccess = file.getParentFile().mkdirs();
            if (dirSuccess) plugin.getLogger().info("Created new plugin directory file!");

            try {
                boolean success = file.createNewFile();
                if (!success) return;

                plugin.getLogger().info("Created messages.yml file!");
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create messages.yml file!");
            }
        }
    }


    public void send(CommandSender sender) {
        message.send(sender);
    }

    public <T extends Iterable<? extends CommandSender>> void send(T iterable) {
        message.send(iterable);
    }

    public void broadcast() {
        message.broadcast();
    }

    public Message replace(String placeholder, String replacement) {
        return message.replace(placeholder, replacement);
    }
}
