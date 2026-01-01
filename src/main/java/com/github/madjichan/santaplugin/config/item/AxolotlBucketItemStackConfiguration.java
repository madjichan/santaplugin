package com.github.madjichan.santaplugin.config.item;

import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;

import java.util.Map;

public class AxolotlBucketItemStackConfiguration {
    private Axolotl.Variant variant;

    private AxolotlBucketItemStackConfiguration() {

    }

    public static AxolotlBucketItemStackConfiguration parse(Map<?, ?> axolotlConfigItem) {
        AxolotlBucketItemStackConfiguration res = new AxolotlBucketItemStackConfiguration();

        if(axolotlConfigItem.containsKey("axolotlVariant")) {
            res.variant = Axolotl.Variant.valueOf((String) axolotlConfigItem.get("axolotlVariant"));
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof AxolotlBucketMeta axolotlBucketMeta)) {
            return item;
        }

        if(this.variant != null)
            axolotlBucketMeta.setVariant(this.variant);

        item.setItemMeta(axolotlBucketMeta);
        return item;
    }
}
