package com.github.madjichan.santaplugin.config.item;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ArmorStandItemStackConfiguration {
    private Boolean isInvisible;
    private Boolean isMarker;
    private Boolean isNoBasePlate;
    private Boolean isShowArms;
    private Boolean isSmall;

    private ArmorStandItemStackConfiguration() {

    }

    public static ArmorStandItemStackConfiguration parse(Map<?, ?> armorStandItemConfig) {
        ArmorStandItemStackConfiguration res = new ArmorStandItemStackConfiguration();

        if(armorStandItemConfig.containsKey("armorStand")) {
            Map<?, ?> armorStandConfig = (Map<?, ?>) armorStandItemConfig.get("armorStand");

            res.isInvisible = (Boolean) armorStandConfig.get("invisible");
            res.isMarker = (Boolean) armorStandConfig.get("marker");
            res.isNoBasePlate = (Boolean) armorStandConfig.get("noBasePlate");
            res.isShowArms = (Boolean) armorStandConfig.get("showArms");
            res.isSmall = (Boolean) armorStandConfig.get("small");
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof ArmorStandMeta armorStandMeta)) {
            return item;
        }

        if(this.isInvisible != null)
            armorStandMeta.setInvisible(this.isInvisible);
        if(this.isMarker != null)
            armorStandMeta.setMarker(this.isMarker);
        if(this.isNoBasePlate != null)
            armorStandMeta.setNoBasePlate(this.isNoBasePlate);
        if(this.isShowArms != null)
            armorStandMeta.setShowArms(this.isShowArms);
        if(this.isSmall != null)
            armorStandMeta.setSmall(this.isSmall);

        item.setItemMeta(armorStandMeta);
        return item;
    }
}
