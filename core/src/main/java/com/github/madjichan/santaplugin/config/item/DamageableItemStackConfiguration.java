package com.github.madjichan.santaplugin.config.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Map;

public class DamageableItemStackConfiguration {
    private Integer damage;

    private DamageableItemStackConfiguration() {

    }

    public static DamageableItemStackConfiguration parse(Map<?, ?> configDamageable) {
        DamageableItemStackConfiguration res = new DamageableItemStackConfiguration();

        if(configDamageable.containsKey("damage"))
            res.damage = (int) configDamageable.get("damage");

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof Damageable damageable)) {
            return item;
        }

        if(this.damage != null)
            damageable.setDamage(this.damage);

        item.setItemMeta(damageable);
        return item;
    }
}
