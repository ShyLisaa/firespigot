package dev.shylisaa.firespigot.plugin.commands;

import dev.shylisaa.firespigot.api.command.Command;
import dev.shylisaa.firespigot.api.command.SpigotCommand;
import dev.shylisaa.firespigot.api.command.context.CommandContext;
import dev.shylisaa.firespigot.api.extension.Extension;
import dev.shylisaa.firespigot.plugin.FireSpigotPlugin;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

@SpigotCommand(name = "modules", permission = "firespigot.modules", noPermissionMessage = """
        §8» §cFireSpigot §8- §7Developed by §cShylisaa
        §8» §cFireSpigot §8- §7Github: §chttps://github.com/Shylisaa/firespigot
        §8» §cFireSpigot §8- §SpigotMC: §chttps://www.spigotmc.org/members/reaktiezz.746653/
        
        §8» §cFireSpigot §8- §7You don't have permission to execute this command.
        """, requiresPlayer = true)
public class ModulesCommand extends Command {

    @Override
    public void execute(CommandContext context) {
        Player player = (Player) context.sender();
        String[] args = context.args();

        switch (args.length) {
            case 0 -> sendHelp(player);
            case 1 -> {
                if (args[0].equalsIgnoreCase("list")) {
                    sendModuleList(player);
                } else {
                    sendHelp(player);
                }
            }
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage("§8» §cFireSpigot §8- §7Help");
        player.sendMessage(" ");
        player.sendMessage("§8» §7/modules list §8- §7List all modules");
        player.sendMessage("§8» §7/modules disable <module> §8- §7Disables a module");
        player.sendMessage(" ");
        player.sendMessage("§7§oMore features will be added soon. Stay tuned!");
    }

    private void sendModuleList(Player player) {
        player.sendMessage("§8» §cFireSpigot §8- §7Modules");
        player.sendMessage(" ");

        for (Extension extension : FireSpigotPlugin.getInstance().getExtensionLoader().getExtensions()) {
            player.sendMessage(MessageFormat.format("§8» §7{0} §8- §7{1}", extension.name(), extension.version()));
        }
    }
}
