package dev.shylisaa.firespigot.api.extension.auth.impl;

import dev.shylisaa.firespigot.api.command.Command;
import dev.shylisaa.firespigot.api.command.CommandBukkitAuth;
import dev.shylisaa.firespigot.api.command.SpigotCommand;
import dev.shylisaa.firespigot.api.command.context.CommandContext;
import dev.shylisaa.firespigot.api.extension.auth.ModuleAuth;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ModuleAuthImpl implements ModuleAuth {

    private final Map<SpigotCommand, Command> commands;
    private final Plugin plugin;

    public ModuleAuthImpl(Plugin plugin) {
        this.commands = new LinkedHashMap<>();
        this.plugin = plugin;
    }

    public Map<SpigotCommand, Command> getCommands() {
        return commands;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void registerCommand(Command command) {
        SpigotCommand spigotCommand = command.getClass().getAnnotation(SpigotCommand.class);
        if (spigotCommand == null) {
            throw new IllegalArgumentException("Command class must be annotated with @SpigotCommand");
        }
        commands.put(spigotCommand, command);
        injectCommand(command);
    }

    @Override
    public void unregisterCommand(Command command) {
        SpigotCommand spigotCommand = command.getClass().getAnnotation(SpigotCommand.class);
        if (spigotCommand == null) {
            throw new IllegalArgumentException("Command class must be annotated with @SpigotCommand");
        }
        commands.remove(spigotCommand);
    }

    @Override
    public <T extends Event> void addListener(Class<T> eventClass, Consumer<T> eventConsumer) {
        Bukkit.getPluginManager().registerEvent(eventClass, new Listener() {
        }, EventPriority.NORMAL, (listener, event) -> {
            if (eventClass.isInstance(event)) {
                eventConsumer.accept((T) event);
            }
        }, this.plugin);
    }

    private void injectCommand(final Command command) {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            final SpigotCommand spigotCommand = command.getClass().getAnnotation(SpigotCommand.class);
            CommandBukkitAuth commandBukkitAuth = new CommandBukkitAuth(spigotCommand.name()) {
                @Override
                public boolean execute(org.bukkit.command.CommandSender sender, String commandLabel, String[] args) {
                    if (!spigotCommand.permission().isEmpty() && !sender.hasPermission(spigotCommand.permission())) {
                        sender.sendMessage(spigotCommand.noPermissionMessage());
                        return true;
                    }

                    if (spigotCommand.requiresPlayer() && !(sender instanceof Player)) {
                        sender.sendMessage("§cYou must be a player to execute this command.");
                        return true;
                    }
                    command.execute(new CommandContext(sender, args, commandLabel));
                    return true;
                }
            };
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(spigotCommand.name(), commandBukkitAuth);
            bukkitCommandMap.setAccessible(false);
            System.out.println("Command " + spigotCommand.name() + " registered.");
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            System.out.println("An error occurred. Please report this on github or spigotmc.");
            exception.printStackTrace();
        }
    }

    private void removeCommand(final Command command) {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            final SpigotCommand spigotCommand = command.getClass().getAnnotation(SpigotCommand.class);
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.getCommand(spigotCommand.name()).unregister(commandMap);
            bukkitCommandMap.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            System.out.println("An error occurred. Please report this on github or SpigotMC.");
            exception.printStackTrace();
        }
    }
}
