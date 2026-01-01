package com.github.madjichan.testapiplugin;

import com.github.madjichan.santaplugin.api.ItemStackGenerator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MyGenerator implements ItemStackGenerator {
    @Override
    public ItemStack configure(String tag, ItemStack item) {
        ItemStack res = new ItemStack(Material.PLAYER_HEAD);
        return item;
    }
}
