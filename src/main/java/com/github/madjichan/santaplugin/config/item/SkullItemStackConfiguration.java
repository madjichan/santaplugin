package com.github.madjichan.santaplugin.config.item;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SkullItemStackConfiguration {
    private SkullItemStackConfiguration() {

    }

    public static SkullItemStackConfiguration parse(Map<?, ?> skullConfigItem) {
        SkullItemStackConfiguration res = new SkullItemStackConfiguration();

        return res;
    }

    public ItemStack configure(ItemStack item) {
        return item;
    }
}
