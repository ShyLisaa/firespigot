package dev.shylisaa.firespigot.api.command;

import dev.shylisaa.firespigot.api.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

public abstract class CommandBukkitAuth extends BukkitCommand {

    public CommandBukkitAuth(String name) {
        super(name);
    }

    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return false;
    }
}
