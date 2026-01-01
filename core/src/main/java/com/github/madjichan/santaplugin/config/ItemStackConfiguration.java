package com.github.madjichan.santaplugin.config;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import com.github.madjichan.santaplugin.api.ItemStackGenerator;
import com.github.madjichan.santaplugin.config.item.*;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ItemStackConfiguration {
    public record EnchantmentRecord(Enchantment ench, int level) {}

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private ItemStackGenerator generator;

    private ArmorItemStackConfiguration armorItemConfig;
    private ArmorStandItemStackConfiguration armorStandItemConfig;
    private AxolotlBucketItemStackConfiguration axolotlBucketItemConfig;
    private BannerItemStackConfiguration bannerItemConfig;
    private BookItemStackConfiguration bookItemConfig;
    private BundleItemStackConfiguration bundleItemConfig;
    private CompassItemStackConfiguration compassItemConfig;
    private CrossbowItemStackConfiguration crossbowItemConfig;
    private DamageableItemStackConfiguration damageableItemConfig;
    private EnchantmentStorageItemStackConfiguration enchantmentStorageItemConfig;
    private FireworkEffectItemStackConfiguration fireworkEffectItemConfig;
    private FireworkItemStackConfiguration fireworkItemConfig;
    private LeatherArmorItemStackConfiguration leatherItemConfig;
    private MusicInstrumentItemStackConfiguration musicInstrumentItemConfig;
    private OminousBottleItemStackConfiguration ominousBottleItemConfig;
    private PotionItemStackConfiguration potionItemConfig;
    private RepairableItemStackConfiguration repairableItemConfig;
    private ShieldItemStackConfiguration shieldItemConfig;
    private SkullItemStackConfiguration skullItemConfig;
    private SuspiciousStewItemStackConfiguration suspiciousStewItemConfig;
    private TropicalFishBucketItemStackConfiguration tropicalFishBucketItemConfig;
    private WritableBookItemStackConfiguration writableBookItemConfig;


    private String tagName;
    Material material;
    Integer amount;
    Component name;
    List<Component> lore;
    List<EnchantmentRecord> enchantments;
    Boolean unbreakable;

    private ItemStackConfiguration() {
        this.lore = new ArrayList<>();
        this.enchantments = new ArrayList<>();
    }

    public static ItemStackConfiguration parse(Map<?, ?> configItem) {
        ItemStackConfiguration res = new ItemStackConfiguration();

        if(configItem.containsKey("generator")) {
//            String pluginName = (String) configItem.get("pluginName");
//            String clazzName = (String) configItem.get("generator");
//
//            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
//
//            Class<?> clazz = null;
//            try {
//                clazz = Class.forName(clazzName, true, plugin.getClass().getClassLoader());
//                if(ItemStackGenerator.class.isAssignableFrom(clazz)) {
//                    res.generator = (ItemStackGenerator) clazz.getDeclaredConstructor().newInstance();
//                }
//            } catch (Exception ex) {
//                // nothing
//            }

            String generatorKeyConfig = (String) configItem.get("generator");

            Collection<RegisteredServiceProvider<ItemStackGenerator>> providers = Bukkit.getServicesManager().getRegistrations(ItemStackGenerator.class);
            for (RegisteredServiceProvider<ItemStackGenerator> provider : providers) {
                ItemStackGenerator instance = provider.getProvider();
                Plugin ownerPlugin = provider.getPlugin();
                String generatorKey = ownerPlugin.getName() + ":" + instance.getClass().getName();
                if(generatorKey.equals(generatorKeyConfig)) {
                    res.generator = instance;
                    break;
                }
            }
        }

        res.tagName = (String) configItem.get("tag");
        if(configItem.containsKey("material"))
            res.material = Material.valueOf((String) configItem.get("material"));
        res.amount = (Integer) configItem.get("amount");
        if(configItem.containsKey("name"))
            res.name = MM.deserialize((String) configItem.get("name"));
        res.unbreakable = (Boolean) configItem.get("unbreakable");

        if(configItem.containsKey("lore")) {
            List<String> configLore = (List<String>) configItem.get("lore");
            for (var configLoreLine : configLore) {
                res.lore.add(MM.deserialize(configLoreLine));
            }
        }

        if(configItem.containsKey("enchantments")) {
            List<Map<?, ?>> configEnchantments = (List<Map<?, ?>>) configItem.get("enchantments");
            for (var configEnchantment : configEnchantments) {
                NamespacedKey key = NamespacedKey.minecraft((String) configEnchantment.get("name"));
                Enchantment ench = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);
                res.enchantments.add(new EnchantmentRecord(ench, (int) configEnchantment.get("level")));
            }
        }

        res.armorItemConfig = ArmorItemStackConfiguration.parse(configItem);
        res.armorStandItemConfig = ArmorStandItemStackConfiguration.parse(configItem);
        res.axolotlBucketItemConfig = AxolotlBucketItemStackConfiguration.parse(configItem);
        res.bannerItemConfig = BannerItemStackConfiguration.parse(configItem);
        res.bookItemConfig = BookItemStackConfiguration.parse(configItem);
        res.bundleItemConfig = BundleItemStackConfiguration.parse(configItem);
        res.compassItemConfig = CompassItemStackConfiguration.parse(configItem);
        res.crossbowItemConfig = CrossbowItemStackConfiguration.parse(configItem);
        res.damageableItemConfig = DamageableItemStackConfiguration.parse(configItem);
        res.enchantmentStorageItemConfig = EnchantmentStorageItemStackConfiguration.parse(configItem);
        res.fireworkEffectItemConfig = FireworkEffectItemStackConfiguration.parse(configItem);
        res.fireworkItemConfig = FireworkItemStackConfiguration.parse(configItem);
        res.leatherItemConfig = LeatherArmorItemStackConfiguration.parse(configItem);
        res.musicInstrumentItemConfig = MusicInstrumentItemStackConfiguration.parse(configItem);
        res.ominousBottleItemConfig = OminousBottleItemStackConfiguration.parse(configItem);
        res.potionItemConfig = PotionItemStackConfiguration.parse(configItem);
        res.repairableItemConfig = RepairableItemStackConfiguration.parse(configItem);
        res.shieldItemConfig = ShieldItemStackConfiguration.parse(configItem);
        res.skullItemConfig = SkullItemStackConfiguration.parse(configItem);
        res.suspiciousStewItemConfig = SuspiciousStewItemStackConfiguration.parse(configItem);
        res.tropicalFishBucketItemConfig = TropicalFishBucketItemStackConfiguration.parse(configItem);
        res.writableBookItemConfig = WritableBookItemStackConfiguration.parse(configItem);

        return res;
    }

    public String getTagName() {
        return this.tagName;
    }

    public ItemStack configure(ItemStack item) {
        if(item == null) {
            item = new ItemStack(this.material, this.amount);
        }
        if(this.amount != null) {
            item = item.asQuantity(this.amount);
        }

        ItemMeta meta = item.getItemMeta();
        if(this.name != null)
            meta.displayName(this.name);
        if(this.unbreakable != null)
            meta.setUnbreakable(this.unbreakable);
        if(!this.lore.isEmpty())
            meta.lore(this.lore);
        for(EnchantmentRecord enchantmentRecord: this.enchantments) {
            meta.addEnchant(enchantmentRecord.ench, enchantmentRecord.level, true);
        }
        item.setItemMeta(meta);

        meta = item.getItemMeta();
        if(meta instanceof ArmorMeta)
            item = this.armorItemConfig.configure(item);
        if(meta instanceof ArmorStandMeta)
            item = this.armorStandItemConfig.configure(item);
        if(meta instanceof AxolotlBucketMeta)
            item = this.axolotlBucketItemConfig.configure(item);
        if(meta instanceof BannerMeta)
            item = this.bannerItemConfig.configure(item);
        if(meta instanceof BookMeta)
            item = this.bookItemConfig.configure(item);
        if(meta instanceof BundleMeta)
            item = this.bundleItemConfig.configure(item);
        if(meta instanceof CompassMeta)
            item = this.compassItemConfig.configure(item);
        if(meta instanceof CrossbowMeta)
            item = this.crossbowItemConfig.configure(item);
        if(meta instanceof Damageable)
            item = this.damageableItemConfig.configure(item);
        if(meta instanceof EnchantmentStorageMeta)
            item = this.enchantmentStorageItemConfig.configure(item);
        if(meta instanceof FireworkEffectMeta)
            item = this.fireworkEffectItemConfig.configure(item);
        if(meta instanceof FireworkMeta)
            item = this.fireworkItemConfig.configure(item);
        if(meta instanceof LeatherArmorMeta)
            item = this.leatherItemConfig.configure(item);
        if(meta instanceof MusicInstrumentMeta)
            item = this.musicInstrumentItemConfig.configure(item);
        if(meta instanceof OminousBottleMeta)
            item = this.ominousBottleItemConfig.configure(item);
        if(meta instanceof PotionMeta)
            item = this.potionItemConfig.configure(item);
        if(meta instanceof Repairable)
            item = this.repairableItemConfig.configure(item);
        if(meta instanceof ShieldMeta)
            item = this.shieldItemConfig.configure(item);
        if(meta instanceof SkullMeta)
            item = this.skullItemConfig.configure(item);
        if(meta instanceof SuspiciousStewMeta)
            item = this.suspiciousStewItemConfig.configure(item);
        if(meta instanceof TropicalFishBucketMeta)
            item = this.tropicalFishBucketItemConfig.configure(item);
        if(meta instanceof WritableBookMeta)
            item = this.writableBookItemConfig.configure(item);

        if(this.generator != null)
            item = this.generator.configure(this.tagName, item);

        return item;
    }
}
