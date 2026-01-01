package com.github.madjichan.santaplugin.config.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.Map;

public class RepairableItemStackConfiguration {
    Integer cost;

    private RepairableItemStackConfiguration() {

    }

    public static RepairableItemStackConfiguration parse(Map<?, ?> repairableItemConfig) {
        RepairableItemStackConfiguration res = new RepairableItemStackConfiguration();

        res.cost = (Integer) repairableItemConfig.get("repairCost");

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof Repairable repairable)) {
            return item;
        }

        if(this.cost != null)
            repairable.setRepairCost(this.cost);

        item.setItemMeta(repairable);
        return item;
    }
}
