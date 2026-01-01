package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.ItemStackConfiguration;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantmentStorageItemStackConfiguration {
    private List<ItemStackConfiguration.EnchantmentRecord> enchantmentRecordList;

    private EnchantmentStorageItemStackConfiguration() {

    }

    public static EnchantmentStorageItemStackConfiguration parse(Map<?, ?> enchStoreItemConfig) {
        EnchantmentStorageItemStackConfiguration res = new EnchantmentStorageItemStackConfiguration();

        if(enchStoreItemConfig.containsKey("enchantmentStorage")) {
            res.enchantmentRecordList = new ArrayList<>();
            List<Map<?, ?>> enchListConfig = (List<Map<?, ?>>) enchStoreItemConfig.get("enchantmentStorage");
            for(Map<?, ?> enchConfig: enchListConfig) {
                String enchKeyName = (String) enchConfig.get("name");
                NamespacedKey enchKey = NamespacedKey.minecraft(enchKeyName);
                Enchantment ench = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(enchKey);

                int level = (int) enchConfig.get("level");

                res.enchantmentRecordList.add(new ItemStackConfiguration.EnchantmentRecord(ench, level));
            }
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta)) {
            return item;
        }

        if(this.enchantmentRecordList != null) {
            for(ItemStackConfiguration.EnchantmentRecord enchantmentRecord: this.enchantmentRecordList) {
                enchantmentStorageMeta.addStoredEnchant(enchantmentRecord.ench(), enchantmentRecord.level(), true);
            }
        }

        item.setItemMeta(enchantmentStorageMeta);
        return item;
    }
}
