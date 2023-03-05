package dev.shylisaa.firespigot.api.command.context;

import org.bukkit.command.CommandSender;

public record CommandContext(CommandSender sender, String[] args, String label) {

}
