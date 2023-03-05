package dev.shylisaa.firespigot.api.command;

import dev.shylisaa.firespigot.api.command.context.CommandContext;

public abstract class Command {

    public abstract void execute(CommandContext context);
}
