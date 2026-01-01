package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.SantaConfiguration;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ShieldMeta;

import java.util.Map;

public class ShieldItemStackConfiguration {
    private DyeColor baseColor;

    private ShieldItemStackConfiguration() {

    }

    public static ShieldItemStackConfiguration parse(Map<?, ?> shieldItemConfig) {
        ShieldItemStackConfiguration res = new ShieldItemStackConfiguration();

        if(shieldItemConfig.containsKey("shieldColor"))
            res.baseColor = DyeColor.valueOf((String) shieldItemConfig.get("shieldColor"));

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof ShieldMeta shieldMeta)) {
            return item;
        }

        if(this.baseColor != null)
            shieldMeta.setBaseColor(this.baseColor);

        item.setItemMeta(shieldMeta);
        return item;
    }
}
