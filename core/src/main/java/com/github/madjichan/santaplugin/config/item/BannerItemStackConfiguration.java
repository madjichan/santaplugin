package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.SantaConfiguration;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BannerItemStackConfiguration {
    private List<Pattern> patterns;

    private BannerItemStackConfiguration() {

    }

    public static BannerItemStackConfiguration parse(Map<?, ?> bannerItemConfig) {
        BannerItemStackConfiguration res = new BannerItemStackConfiguration();

        if(bannerItemConfig.containsKey("bannerPatterns")) {
            res.patterns = new ArrayList<>();
            List<Map<?, ?>> patternListConfig = (List<Map<?, ?>>) bannerItemConfig.get("bannerPatterns");

            for(Map<?, ?> patternConfig: patternListConfig) {
                String patternTypeKeyName = (String) patternConfig.get("type");
                NamespacedKey patternTypeKey = NamespacedKey.minecraft(patternTypeKeyName);
                PatternType patternType = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).get(patternTypeKey);

                DyeColor color = DyeColor.valueOf((String) patternConfig.get("color"));

                res.patterns.add(new Pattern(color, patternType));
            }
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof BannerMeta bannerMeta)) {
            return item;
        }

        if(this.patterns != null) {
            bannerMeta.setPatterns(this.patterns);
        }

        item.setItemMeta(bannerMeta);
        return item;
    }
}
