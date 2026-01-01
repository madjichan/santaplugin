package com.github.madjichan.santaplugin.config.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.WritableBookMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WritableBookItemStackConfiguration {
    private List<String> pages;

    private WritableBookItemStackConfiguration() {

    }

    public static WritableBookItemStackConfiguration parse(Map<?, ?> writableItemConfig) {
        WritableBookItemStackConfiguration res = new WritableBookItemStackConfiguration();

        if(writableItemConfig.containsKey("pages"))
            res.pages = (List<String>) writableItemConfig.get("pages");

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof WritableBookMeta writableBookMeta)) {
            return item;
        }

        if(this.pages != null)
            writableBookMeta.setPages(this.pages);

        item.setItemMeta(writableBookMeta);
        return item;
    }
}
