package com.github.madjichan.santaplugin.config.item;

import io.papermc.paper.datacomponent.item.SuspiciousStewEffects;
import io.papermc.paper.potion.SuspiciousEffectEntry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;

public class SuspiciousStewItemStackConfiguration {
    private List<PotionEffect> effects;

    private SuspiciousStewItemStackConfiguration() {

    }

    public static SuspiciousStewItemStackConfiguration parse(Map<?, ?> suspiciousStewItemConfig) {
        SuspiciousStewItemStackConfiguration res = new SuspiciousStewItemStackConfiguration();

        if(suspiciousStewItemConfig.containsKey("suspiciousStewEffects"))
            res.effects = PotionItemStackConfiguration.parsePotionEffects((List<Map<?, ?>>) suspiciousStewItemConfig.get("suspiciousStewEffects"));

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof SuspiciousStewMeta suspiciousStewMeta)) {
            return item;
        }

        if(this.effects != null) {
            for(PotionEffect effect: this.effects) {
                suspiciousStewMeta.addCustomEffect(SuspiciousEffectEntry.create(effect.getType(), effect.getDuration()), true);
            }
        }

        item.setItemMeta(suspiciousStewMeta);
        return item;
    }
}
