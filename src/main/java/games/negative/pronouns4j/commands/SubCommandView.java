package games.negative.pronouns4j.commands;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandProperties;
import games.negative.alumina.command.Context;
import games.negative.alumina.util.ColorUtil;
import games.negative.pronouns4j.Locale;
import games.negative.pronouns4j.Pronouns4J;
import games.negative.pronouns4j.pronouns.Pronouns;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubCommandView extends Command {

    public SubCommandView() {
        super(CommandProperties.builder()
                .name("view")
                .description("View your pronouns")
                .playerOnly(true)
                .build());
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player().orElseThrow();

        Pronouns pronouns = Pronouns4J.getInstance().getPronounsManager().getPronouns(player.getUniqueId().toString());

        if (pronouns == null) {
            player.sendMessage(ColorUtil.translate("&cYou have not set your pronouns yet! Use &7/pronouns set <subjective> <objective> <possessive> <reflexive>"));
            return;
        }

        String subjective = capitalizeFirstLetter(pronouns.getSubjective());
        String objective = capitalizeFirstLetter(pronouns.getObjective());
        String possessive = capitalizeFirstLetter(pronouns.getPossessive());
        String reflexive = capitalizeFirstLetter(pronouns.getReflexive());

        String pronounsString = String.format("%s/%s/%s/%s", subjective, objective, possessive, reflexive);

        Locale.VIEW_PRONOUNS
                .replace("%sub%", subjective)
                .replace("%obj%", objective)
                .replace("%pos%", possessive)
                .replace("%ref%", reflexive)
                .replace("%pronouns%", pronounsString)
                .send(player);
    }
}
