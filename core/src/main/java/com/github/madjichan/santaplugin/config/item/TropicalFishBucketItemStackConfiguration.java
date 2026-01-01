package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.SantaConfiguration;
import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

import java.util.Map;

public class TropicalFishBucketItemStackConfiguration {
    private DyeColor bodyColor;
    private TropicalFish.Pattern pattern;
    private DyeColor patternColor;

    private TropicalFishBucketItemStackConfiguration() {

    }

    public static TropicalFishBucketItemStackConfiguration parse(Map<?, ?> tropicalItemConfig) {
        TropicalFishBucketItemStackConfiguration res = new TropicalFishBucketItemStackConfiguration();

        if(tropicalItemConfig.containsKey("tropicalFish")) {
            Map<?, ?> tropicalConfig = (Map<?, ?>) tropicalItemConfig.get("tropicalFish");

            res.bodyColor = DyeColor.valueOf((String) tropicalConfig.get("bodyColor"));
            res.patternColor = DyeColor.valueOf((String) tropicalConfig.get("patternColor"));
            res.pattern = TropicalFish.Pattern.valueOf((String) tropicalConfig.get("pattern"));

        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof TropicalFishBucketMeta tropicalFishBucketMeta)) {
            return item;
        }

        if(this.bodyColor != null)
            tropicalFishBucketMeta.setBodyColor(this.bodyColor);
        if(this.pattern != null)
            tropicalFishBucketMeta.setPattern(this.pattern);
        if(this.patternColor != null)
            tropicalFishBucketMeta.setPatternColor(this.patternColor);

        item.setItemMeta(tropicalFishBucketMeta);
        return item;
    }
}
