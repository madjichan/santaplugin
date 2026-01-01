package com.github.madjichan.santaplugin.config.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookItemStackConfiguration {
    private Component title;
    private Component author;
    private List<Component> pages;

    private BookItemStackConfiguration() {

    }

    public static List<Component> parsePages(List<String> pageListConfig) {
        return pageListConfig.stream()
                .map(pageConfig -> { return MiniMessage.miniMessage().deserialize(pageConfig); })
                .collect(Collectors.toList());
    }

    public static BookItemStackConfiguration parse(Map<?, ?> bookItemConfig) {
        BookItemStackConfiguration res = new BookItemStackConfiguration();

        if(bookItemConfig.containsKey("book")) {
            Map<?, ?> bookConfig = (Map<?, ?>) bookItemConfig.get("book");

            res.title = MiniMessage.miniMessage().deserialize((String) bookConfig.get("title"));
            res.author = MiniMessage.miniMessage().deserialize((String) bookConfig.get("author"));
            res.pages = parsePages((List<String>) bookConfig.get("pages"));
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof BookMeta bookMeta)) {
            return item;
        }

        if(this.title != null)
            bookMeta.title(this.title);
        if(this.author != null)
            bookMeta.author(this.author);
        if(this.pages != null)
            bookMeta.addPages(this.pages.toArray(Component[]::new));

        item.setItemMeta(bookMeta);
        return item;
    }
}
