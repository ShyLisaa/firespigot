package dev.shylisaa.firespigot.api.extension.loader;

import dev.shylisaa.firespigot.api.extension.Extension;
import dev.shylisaa.firespigot.api.extension.auth.ModuleAuth;
import dev.shylisaa.firespigot.api.extension.inject.Inject;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ExtensionLoader {

    private final List<Extension> extensions;

    public ExtensionLoader() {
        this.extensions = new LinkedList<>();
        Path pluginPath = Path.of("plugins/FireSpigot");
        Path extensionPath = Path.of("plugins/FireSpigot/extensions");

        if(Files.notExists(pluginPath)){
            try {
                Files.createDirectory(pluginPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Files.notExists(extensionPath)){
            try {
                Files.createDirectory(extensionPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadExtensions(){
        try {

            File[] files = new File("plugins/FireSpigot/extensions").listFiles();
            if(files == null) return;

            for(File file : Arrays.stream(files).filter(Objects::nonNull).filter(this::isValidJarFile).toList()){

                try (JarFile jarFile = new JarFile(file)){
                    JarEntry jarEntry = jarFile.getJarEntry("extension.json");

                    if(jarEntry == null) return;

                    try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {

                        Extension extension = Extension.fromJson(inputStream, file);

                        extensions.add(extension);

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ensureExtensions(ModuleAuth moduleAuth){
        extensions.forEach(extension -> {
            try {

                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new File("plugins/FireSpigot/extensions", extension.fileName()).toURI().toURL()}, getClass().getClassLoader());

                Class<?> extensionClass = Class.forName(extension.main(), true, urlClassLoader);

                if(extensionClass.isEnum() || extensionClass.isAnnotation() || extensionClass.isInterface() || extensionClass.isRecord()){
                    Bukkit.getConsoleSender().sendMessage("§c§l! §7The extension " + extension.name() + " is not a class!");
                    return;
                }

                if(extensionClass.getAnnotation(Inject.class) == null){
                    Bukkit.getConsoleSender().sendMessage("§c§l! §7The extension " + extension.name() + " is not annotated with @Inject!");
                    return;
                }

                Object instance = extensionClass.getDeclaredConstructor().newInstance();

                Inject inject = extensionClass.getAnnotation(Inject.class);

                if(!inject.name().equals(extension.name())){
                    Bukkit.getConsoleSender().sendMessage("§c§l! §7The extension " + extension.name() + " has an invalid name!");
                    return;
                }

                Method method = extensionClass.getMethod("inject", ModuleAuth.class);
                method.invoke(instance, moduleAuth);

                Bukkit.getConsoleSender().sendMessage("§a§l✔  §7The extension " + extension.name() + " has been loaded successfully!");

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | MalformedURLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public void unloadExtensions(ModuleAuth moduleAuth){
        extensions.forEach(extension -> {
            try {

                Class<?> extensionClass = Class.forName(extension.main());

                Object instance = extensionClass.getDeclaredConstructor().newInstance();

                Method method = extensionClass.getMethod("uninject", ModuleAuth.class);
                method.invoke(instance, moduleAuth);

                Bukkit.getConsoleSender().sendMessage("§a§l✔  §7The extension " + extension.name() + " has been unloaded successfully!");

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public boolean isValidJarFile(File file){
        return file.getName().endsWith(".jar");
    }

    public List<Extension> getExtensions() {
        return extensions;
    }
}