package com.github.madjichan.santaplugin.config.item;

import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FireworkItemStackConfiguration {
    private Integer power;
    private List<FireworkEffect> effects;

    private FireworkItemStackConfiguration() {

    }

    public static FireworkItemStackConfiguration parse(Map<?, ?> fireworkItemConfig) {
        FireworkItemStackConfiguration res = new FireworkItemStackConfiguration();

        if(fireworkItemConfig.containsKey("power"))
            res.power = (Integer) fireworkItemConfig.get("power");

        if(fireworkItemConfig.containsKey("effects")) {
            res.effects = new ArrayList<>();
            List<Map<?, ?>> effectListConfig = (List<Map<?, ?>>) fireworkItemConfig.get("effects");
            for(Map<?, ?> effectConfig: effectListConfig) {
                FireworkEffect fireworkEffect = FireworkEffectItemStackConfiguration.parseFireworkEffect(effectConfig);
                res.effects.add(fireworkEffect);
            }
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof FireworkMeta fireworkMeta)) {
            return item;
        }

        if(this.power != null)
            fireworkMeta.setPower(this.power);
        if(this.effects != null)
            fireworkMeta.addEffects(this.effects);

        item.setItemMeta(fireworkMeta);
        return item;
    }
}
