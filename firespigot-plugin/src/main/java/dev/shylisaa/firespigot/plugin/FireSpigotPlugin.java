package dev.shylisaa.firespigot.plugin;

import dev.shylisaa.firespigot.api.extension.auth.ModuleAuth;
import dev.shylisaa.firespigot.api.extension.auth.impl.ModuleAuthImpl;
import dev.shylisaa.firespigot.api.extension.loader.ExtensionLoader;
import dev.shylisaa.firespigot.plugin.commands.ModulesCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class FireSpigotPlugin extends JavaPlugin {

    private static FireSpigotPlugin instance;
    private ModuleAuth auth;
    private ExtensionLoader extensionLoader;

    @Override
    public void onEnable() {
        instance = this;
        this.auth = new ModuleAuthImpl(this);

        this.extensionLoader = new ExtensionLoader();
        this.extensionLoader.loadExtensions();
        this.extensionLoader.ensureExtensions(this.auth);

        this.auth.registerCommand(new ModulesCommand());

        System.out.println("FireSpigot is ready!");
    }

    @Override
    public void onDisable() {

    }

    public static FireSpigotPlugin getInstance() {
        return instance;
    }

    public ModuleAuth getAuth() {
        return auth;
    }

    public ExtensionLoader getExtensionLoader() {
        return extensionLoader;
    }
}
