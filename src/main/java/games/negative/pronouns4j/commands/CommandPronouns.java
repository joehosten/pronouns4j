package games.negative.pronouns4j.commands;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandProperties;
import games.negative.alumina.command.Context;
import games.negative.pronouns4j.Locale;
import org.jetbrains.annotations.NotNull;

public class CommandPronouns extends Command {
    public CommandPronouns() {
        super(CommandProperties.builder().name("pronouns").build());

        addSubCommand(new SubCommandSet());
        addSubCommand(new SubCommandClear());
        addSubCommand(new SubCommandView());
    }

    @Override
    public void execute(@NotNull Context context) {
        Locale.TITLE_HELP.send(context.sender());
    }
}
