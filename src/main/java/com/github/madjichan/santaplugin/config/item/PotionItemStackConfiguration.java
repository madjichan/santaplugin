package com.github.madjichan.santaplugin.config.item;

import com.github.madjichan.santaplugin.config.SantaConfiguration;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PotionItemStackConfiguration {
    private Component customName;
    private PotionType baseType;
    private List<PotionEffect> effects;
    private Color color;

    private PotionItemStackConfiguration() {

    }

    public static List<PotionEffect> parsePotionEffects(List<Map<?, ?>> effectListConfig) {
        List<PotionEffect> res = new ArrayList<>();

        for(Map<?, ?> effectConfig: effectListConfig) {
            String effectKeyName = (String) effectConfig.get("type");
            NamespacedKey effectKey = NamespacedKey.minecraft(effectKeyName);
            PotionEffectType effectType = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(effectKey);

            int duration, amplifier;
            boolean ambient, particles, icon;
            duration = (int) effectConfig.get("duration");
            amplifier = (int) effectConfig.get("amplifier");
            ambient = (boolean) effectConfig.get("ambient");
            particles = (boolean) effectConfig.get("particles");
            icon = (boolean) effectConfig.get("icon");

            res.add(new PotionEffect(effectType, duration, amplifier, ambient, particles, icon));
        }

        return res;
    }

    public static PotionItemStackConfiguration parse(Map<?, ?> potionItemConfig) {
        PotionItemStackConfiguration res = new PotionItemStackConfiguration();

        if(potionItemConfig.containsKey("potion")) {
            Map<?, ?> potionConfig = (Map<?, ?>) potionItemConfig.get("potion");

            if(potionConfig.containsKey("customName"))
                res.customName = MiniMessage.miniMessage().deserialize((String) potionConfig.get("customName"));

            res.color = SantaConfiguration.hexToColor((String) potionConfig.get("color"));
            res.baseType = PotionType.valueOf((String) potionConfig.get("baseType"));

            List<Map<?, ?>> effectListConfig = (List<Map<?, ?>>) potionConfig.get("effects");
            res.effects = parsePotionEffects(effectListConfig);
        }

        return res;
    }

    public ItemStack configure(ItemStack item) {
        if(!(item.getItemMeta() instanceof PotionMeta potionMeta)) {
            return item;
        }

        if(this.customName != null)
            potionMeta.customName(this.customName);
        if(this.baseType != null)
            potionMeta.setBasePotionType(this.baseType);
        if(this.color != null)
            potionMeta.setColor(this.color);
        if(this.effects != null) {
            for(PotionEffect effect: this.effects) {
                potionMeta.addCustomEffect(effect, true);
            }
        }

        item.setItemMeta(potionMeta);
        return item;
    }
}
