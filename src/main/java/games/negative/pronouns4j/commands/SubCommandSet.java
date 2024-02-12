package games.negative.pronouns4j.commands;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandProperties;
import games.negative.alumina.command.Context;
import games.negative.pronouns4j.Locale;
import games.negative.pronouns4j.Pronouns4J;
import games.negative.pronouns4j.pronouns.Pronouns;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubCommandSet extends Command {

    public SubCommandSet() {
        super(CommandProperties.builder()
                .name("set")
                .description("Set your pronouns")
                .usage("/pronouns set <subjective> <objective> <possessive> <reflexive>")
                .playerOnly(true)
                .params(List.of("subjective", "objective", "possessive", "reflexive"))
                .build());
    }

    @Override
    public void execute(@NotNull Context context) {

        Player player = (Player) context.sender();
        String[] args = context.args();

        String subjective = args[0];
        String objective = args[1];
        String possessive = args[2];
        String reflexive = args[3];

        Pronouns pronouns = new Pronouns(subjective, objective, possessive, reflexive);
        Pronouns4J.getInstance().getPronounsManager().savePronouns(player.getUniqueId().toString(), pronouns);

        Locale.SET_PRONOUNS
                .replace("%pronouns%", pronouns.getSubjective() + "/" + pronouns.getObjective() + "/" + pronouns.getPossessive())
                .send(player);
    }
}
