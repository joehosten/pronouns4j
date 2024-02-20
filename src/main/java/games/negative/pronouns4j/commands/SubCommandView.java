package games.negative.pronouns4j.commands;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandProperties;
import games.negative.alumina.command.Context;
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

    @Override
    public void execute(@NotNull Context context) {

        Player player = (Player) context.sender();
        Pronouns pronouns = Pronouns4J.getInstance().getPronounsManager().getPronouns(player.getUniqueId().toString());

        Locale.VIEW_PRONOUNS.replace("%sub%", capitalizeFirstLetter(pronouns.getSubjective()))
                .replace("%obj%", pronouns.getObjective())
                .replace("%pos%", capitalizeFirstLetter(pronouns.getPossessive()))
                .replace("%ref%", pronouns.getReflexive())
                .replace("%pronouns%", pronouns.getSubjective() + "/" + pronouns.getObjective() + "/" + pronouns.getPossessive() + "/" + pronouns.getReflexive())
                .send(player);
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
