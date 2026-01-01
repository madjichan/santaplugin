package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.SantaConfiguration;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Map;

public class LeatherArmorItemStackConfiguration {
    private Color color;

    private LeatherArmorItemStackConfiguration() {

    }

    public static LeatherArmorItemStackConfiguration parse(Map<?, ?> leatherConfigItem) {
        LeatherArmorItemStackConfiguration res = new LeatherArmorItemStackConfiguration();

        if(leatherConfigItem.containsKey("color"))
            res.color = SantaConfiguration.hexToColor((String) leatherConfigItem.get("color"));

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof LeatherArmorMeta leatherArmorMeta)) {
            return item;
        }
        if(this.color != null)
            leatherArmorMeta.setColor(color);

        item.setItemMeta(leatherArmorMeta);
        return item;
    }
}
