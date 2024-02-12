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
            return; // Exit if the sender is not a player
        }

        if (context.args().length == 0) {
            // If no arguments provided, clear pronouns for the sender
            clearPronouns(senderPlayer, senderPlayer);
        } else {
            Player targetPlayer = Bukkit.getPlayer(context.args()[0]);
            if (targetPlayer == null) {
                Locale.PLAYER_NOT_FOUND.send(senderPlayer);
            } else {
                // Clear pronouns for the specified player
                clearPronouns(targetPlayer, senderPlayer);
            }
        }
    }

    // Helper method to get the sender player, returning null if not a player
    private Player getPlayerSender(Context context) {
        if (!(context.sender() instanceof Player)) {
            context.sender().sendMessage("You must be a player to use this command!");
            return null;
        }
        return (Player) context.sender();
    }

    // Method to clear pronouns for a player
    private void clearPronouns(Player targetPlayer, Player executor) {
        Pronouns4J.getInstance().getPronounsManager().resetPronouns(targetPlayer.getUniqueId().toString());
        // Notify the executor about the action
        if (targetPlayer.equals(executor) || executor.hasPermission("pronouns4j.admin")) {
            Locale.CLEAR_SELF_PRONOUNS.send(targetPlayer); // Inform the target player about pronouns being cleared
            if (!targetPlayer.equals(executor)) {
                Locale.CLEAR_OTHER_PRONOUNS.replace("%player%", targetPlayer.getName()).send(executor);
            }
        } else {
            Locale.NO_PERMISSION.send(executor); // Notify the executor about insufficient permission
        }
    }
}
