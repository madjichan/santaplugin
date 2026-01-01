package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.SantaConfiguration;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import java.util.List;
import java.util.Map;

public class FireworkEffectItemStackConfiguration {
    private FireworkEffect fireworkEffect;

    private FireworkEffectItemStackConfiguration() {

    }

    public static FireworkEffect parseFireworkEffect(Map<?, ?> fireworkEffectConfig) {
        FireworkEffect.Builder builder = FireworkEffect.builder();

        if(fireworkEffectConfig.containsKey("flicker"))
            builder.flicker((boolean) fireworkEffectConfig.get("flicker"));
        if(fireworkEffectConfig.containsKey("trail"))
            builder.trail((boolean) fireworkEffectConfig.get("trail"));
        if(fireworkEffectConfig.containsKey("type"))
            builder.with(FireworkEffect.Type.valueOf((String) fireworkEffectConfig.get("type")));

        if(fireworkEffectConfig.containsKey("colors")) {
            List<String> colorListConfig = (List<String>) fireworkEffectConfig.get("colors");
            for(String colorConfig: colorListConfig) {
                builder.withColor(SantaConfiguration.hexToColor(colorConfig));
            }
        }

        if(fireworkEffectConfig.containsKey("fades")) {
            List<String> fadeListConfig = (List<String>) fireworkEffectConfig.get("fades");
            for(String fadeConfig: fadeListConfig) {
                builder.withFade(SantaConfiguration.hexToColor(fadeConfig));
            }
        }

        return builder.build();
    }

    public static FireworkEffectItemStackConfiguration parse(Map<?, ?> fireworkEffectItemConfig) {
        FireworkEffectItemStackConfiguration res = new FireworkEffectItemStackConfiguration();

        if(fireworkEffectItemConfig.containsKey("fireworkEffect"))
            res.fireworkEffect = parseFireworkEffect((Map<?, ?>) fireworkEffectItemConfig.get("fireworkEffect"));

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof FireworkEffectMeta fireworkEffectMeta)) {
            return item;
        }

        if(this.fireworkEffect != null)
            fireworkEffectMeta.setEffect(this.fireworkEffect);

        item.setItemMeta(fireworkEffectMeta);
        return item;
    }
}
