package dev.shylisaa.firespigot.api.extension.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

    String name();

    String version();
}
