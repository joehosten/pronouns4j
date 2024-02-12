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

        Locale.VIEW_PRONOUNS.replace("%sub%", pronouns.getSubjective())
                .replace("%obj%", pronouns.getObjective())
                .replace("%pos%", pronouns.getPossessive())
                .replace("%ref%", pronouns.getReflexive())
                .send(player);
    }
}
