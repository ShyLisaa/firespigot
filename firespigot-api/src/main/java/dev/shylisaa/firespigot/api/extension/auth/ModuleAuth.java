package dev.shylisaa.firespigot.api.extension.auth;

import dev.shylisaa.firespigot.api.command.Command;
import dev.shylisaa.firespigot.api.command.SpigotCommand;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.function.Consumer;

public interface ModuleAuth {

    void registerCommand(Command command);

    void unregisterCommand(Command command);

    <T extends Event> void addListener(Class<T> eventClass, Consumer<T> eventConsumer);

    Plugin getPlugin();


}
