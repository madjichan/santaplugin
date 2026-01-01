package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.ItemStackConfiguration;
import com.github.madjichan.santaplugin.config.SantaConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BundleItemStackConfiguration {
    private List<ItemStackConfiguration> bundleItems;

    private BundleItemStackConfiguration() {

    }

    public static BundleItemStackConfiguration parse(Map<?, ?> bundleItemConfig) {
        BundleItemStackConfiguration res = new BundleItemStackConfiguration();

        if(bundleItemConfig.containsKey("bundleItems")) {
            res.bundleItems = new ArrayList<>();
            List<Map<?, ?>> itemListConfig = (List<Map<?, ?>>) bundleItemConfig.get("bundleItems");
            for(Map<?, ?> itemConfig: itemListConfig) {
                res.bundleItems.add(ItemStackConfiguration.parse(itemConfig));
            }
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof BundleMeta bundleMeta)) {
            return item;
        }

        if(this.bundleItems != null) {
            List<ItemStack> bundleItems = new ArrayList<>();
            for(ItemStackConfiguration itemConfig: this.bundleItems) {
                ItemStack bundleItem = SantaConfiguration.getInstance().items.get(itemConfig.getTagName()).configure(null);
                bundleItem = itemConfig.configure(bundleItem);
                bundleItems.add(bundleItem);
            }
            bundleMeta.setItems(bundleItems);
        }

        item.setItemMeta(bundleMeta);
        return item;
    }
}
