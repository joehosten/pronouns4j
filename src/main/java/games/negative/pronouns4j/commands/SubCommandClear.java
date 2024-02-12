package games.negative.pronouns4j.commands;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandProperties;
import games.negative.alumina.command.Context;
import games.negative.pronouns4j.Locale;
import games.negative.pronouns4j.Pronouns4J;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubCommandClear extends Command {

    public SubCommandClear() {
        super(CommandProperties.builder()
                .name("clear")
                .description("Clears your pronouns to the default (they/them)")
                .build());
    }

    @Override
    public void execute(@NotNull Context context) {
        Player senderPlayer = getPlayerSender(context);
        if (senderPlayer == null) {
            return;
        }

        if (context.args().length == 0) {
            clearPronouns(senderPlayer, (Player) context.sender());
        } else {
            Player targetPlayer = Bukkit.getPlayer(context.args()[0]);
            if (targetPlayer == null) {
                Locale.PLAYER_NOT_FOUND.send(senderPlayer);
            } else {
                clearPronouns(targetPlayer, senderPlayer);
            }
        }
    }

    private Player getPlayerSender(Context context) {
        if (!(context.sender() instanceof Player)) {
            context.sender().sendMessage("You must be a player to use this command!");
            return null;
        }
        return (Player) context.sender();
    }

    private void clearPronouns(Player player, Player executor) {
        Pronouns4J.getInstance().getPronounsManager().resetPronouns(player.getUniqueId().toString());
        if (player.equals(executor)) {
            Locale.CLEAR_SELF_PRONOUNS.send(player);
        } else {
            Locale.CLEAR_OTHER_PRONOUNS.replace("%player%", player.getName()).send(executor);
        }
    }
}
