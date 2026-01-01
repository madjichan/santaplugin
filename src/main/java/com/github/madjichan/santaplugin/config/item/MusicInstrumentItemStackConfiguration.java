package com.github.madjichan.santaplugin.config.item;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MusicInstrumentMeta;

import java.util.Map;

public class MusicInstrumentItemStackConfiguration {
    private MusicInstrument musicInstrument;

    private MusicInstrumentItemStackConfiguration() {

    }

    public static MusicInstrumentItemStackConfiguration parse(Map<?, ?> musicInstrumentItemConfig) {
        MusicInstrumentItemStackConfiguration res = new MusicInstrumentItemStackConfiguration();

        if(musicInstrumentItemConfig.containsKey("musicInstrument")) {
            String musicInstrumentKeyName = (String) musicInstrumentItemConfig.get("musicInstrument");
            NamespacedKey musicInstrumentKey = NamespacedKey.minecraft(musicInstrumentKeyName);
            res.musicInstrument = RegistryAccess.registryAccess().getRegistry(RegistryKey.INSTRUMENT).get(musicInstrumentKey);
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof MusicInstrumentMeta musicInstrumentMeta)) {
            return item;
        }

        if(this.musicInstrument != null)
            musicInstrumentMeta.setInstrument(this.musicInstrument);

        item.setItemMeta(musicInstrumentMeta);
        return item;
    }
}
