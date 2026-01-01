package com.github.madjichan.santaplugin.api;

import org.bukkit.inventory.ItemStack;

public interface ItemStackGenerator {
    ItemStack configure(String tag, ItemStack item);
}