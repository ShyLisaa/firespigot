package dev.shylisaa.firespigot.plugin;

import dev.shylisaa.firespigot.api.extension.auth.ModuleAuth;
import dev.shylisaa.firespigot.api.extension.auth.impl.ModuleAuthImpl;
import dev.shylisaa.firespigot.api.extension.loader.ExtensionLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FireSpigotPlugin extends JavaPlugin {

    private ModuleAuth auth;
    private ExtensionLoader extensionLoader;

    @Override
    public void onEnable() {
        this.auth = new ModuleAuthImpl(this);

        this.extensionLoader = new ExtensionLoader();
        this.extensionLoader.loadExtensions();
        this.extensionLoader.ensureExtensions(this.auth);
    }

    @Override
    public void onDisable() {

    }

    public ModuleAuth getAuth() {
        return auth;
    }
}
