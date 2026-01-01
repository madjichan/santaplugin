package com.github.madjichan.testapiplugin;

import com.github.madjichan.santaplugin.api.ItemStackGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        // Register MULTIPLE implementations
        Bukkit.getServicesManager().register(
                ItemStackGenerator.class,
                (ItemStackGenerator) new MyGenerator(),
                this,
                ServicePriority.Normal
        );
    }
}


