package com.github.madjichan.santaplugin.config.item;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.Map;

public class ArmorItemStackConfiguration {
    private ArmorTrim armorTrim;

    private ArmorItemStackConfiguration() {

    }

    public static ArmorItemStackConfiguration parse(Map<?, ?> armorConfigItem) {
        ArmorItemStackConfiguration res = new ArmorItemStackConfiguration();

        if(armorConfigItem.containsKey("armor")) {
            Map<?, ?> armorConfig = (Map<?, ?>) armorConfigItem.get("armor");

            String materialKeyName, patternKeyName;
            materialKeyName = (String) armorConfig.get("material");
            patternKeyName = (String) armorConfig.get("pattern");
            NamespacedKey materialKey = NamespacedKey.minecraft(materialKeyName);
            NamespacedKey patternKey = NamespacedKey.minecraft(patternKeyName);

            TrimMaterial material = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(materialKey);
            TrimPattern pattern = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(patternKey);
            res.armorTrim = new ArmorTrim(material, pattern);
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof ArmorMeta armorMeta)) {
            return item;
        }

        if(this.armorTrim != null)
            armorMeta.setTrim(this.armorTrim);

        item.setItemMeta(armorMeta);
        return item;
    }
}
