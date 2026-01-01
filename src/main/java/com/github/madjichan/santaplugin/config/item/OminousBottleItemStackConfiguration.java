package com.github.madjichan.santaplugin.config.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.OminousBottleMeta;

import java.util.Map;

public class OminousBottleItemStackConfiguration {
    Integer amplifier;

    private OminousBottleItemStackConfiguration() {

    }

    public static OminousBottleItemStackConfiguration parse(Map<?, ?> ominousBottleItemConfig) {
        OminousBottleItemStackConfiguration res = new OminousBottleItemStackConfiguration();

        if(ominousBottleItemConfig.containsKey("ominousAmplifier"))
            res.amplifier = (Integer) ominousBottleItemConfig.get("ominousAmplifier");

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof OminousBottleMeta ominousBottleMeta)) {
            return item;
        }

        if(this.amplifier != null)
            ominousBottleMeta.setAmplifier(this.amplifier);

        item.setItemMeta(ominousBottleMeta);
        return item;
    }
}
