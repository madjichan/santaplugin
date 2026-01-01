package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.ItemStackConfiguration;
import com.github.madjichan.santaplugin.config.SantaConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrossbowItemStackConfiguration {
    private List<ItemStackConfiguration> crossbowItems;

    private CrossbowItemStackConfiguration() {

    }

    public static CrossbowItemStackConfiguration parse(Map<?, ?> crossbowItemConfig) {
        CrossbowItemStackConfiguration res = new CrossbowItemStackConfiguration();

        if(crossbowItemConfig.containsKey("crossbowItems")) {
            res.crossbowItems = new ArrayList<>();
            List<Map<?, ?>> itemListConfig = (List<Map<?, ?>>) crossbowItemConfig.get("crossbowItems");
            for(Map<?, ?> itemConfig: itemListConfig) {
                res.crossbowItems.add(ItemStackConfiguration.parse(itemConfig));
            }
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof CrossbowMeta crossbowMeta)) {
            return item;
        }

        if(this.crossbowItems != null) {
            List<ItemStack> crossbowItems = new ArrayList<>();
            for(ItemStackConfiguration itemConfig: this.crossbowItems) {
                ItemStack crossbowItem = SantaConfiguration.getInstance().items.get(itemConfig.getTagName()).configure(null);
                crossbowItem = itemConfig.configure(crossbowItem);
                crossbowItems.add(crossbowItem);
            }
            crossbowMeta.setChargedProjectiles(crossbowItems);
        }

        item.setItemMeta(crossbowMeta);
        return item;
    }
}
