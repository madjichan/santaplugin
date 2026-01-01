package com.github.madjichan.santaplugin.config.item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.Map;

public class CompassItemStackConfiguration {
    private Boolean tracked;
    private Location compassAt;

    private CompassItemStackConfiguration() {

    }

    public static CompassItemStackConfiguration parse(Map<?, ?> compassItemConfig) {
        CompassItemStackConfiguration res = new CompassItemStackConfiguration();

        if(compassItemConfig.containsKey("compassTracked"))
            res.tracked = (Boolean) compassItemConfig.get("compassTracked");

        if(compassItemConfig.containsKey("compassAt")) {
            Map<?, ?> compassConfig = (Map<?, ?>) compassItemConfig.get("compassAt");

            World world;
            double x, y, z;
            world = Bukkit.getWorld((String) compassConfig.get("world"));
            x = (double) compassConfig.get("x");
            y = (double) compassConfig.get("y");
            z = (double) compassConfig.get("z");

            res.compassAt = new Location(world, x, y, z);
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof CompassMeta compassMeta)) {
            return item;
        }

        if(this.tracked != null)
            compassMeta.setLodestoneTracked(this.tracked);
        if(this.compassAt != null)
            compassMeta.setLodestone(this.compassAt);

        item.setItemMeta(compassMeta);
        return item;
    }
}
