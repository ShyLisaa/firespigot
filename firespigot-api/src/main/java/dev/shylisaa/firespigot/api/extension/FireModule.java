package dev.shylisaa.firespigot.api.extension;

import dev.shylisaa.firespigot.api.extension.auth.ModuleAuth;

public abstract class FireModule {


    public abstract void inject(ModuleAuth auth);

    public abstract void uninject();
}
