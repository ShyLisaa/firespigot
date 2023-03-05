package dev.shylisaa.firespigot.api.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SpigotCommand {

    String name();

    String permission() default "";

    String noPermissionMessage() default "§cYou don't have permission to execute this command.";

    boolean requiresPlayer() default false;
}
