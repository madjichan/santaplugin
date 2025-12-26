package com.github.madjichan.santaplugin.present;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.Bukkit.getServer;

public class Presents implements Listener {
    private static Presents instance;
    private final JavaPlugin plugin;
    private final NamespacedKey presentFallingKey, presentLyingKey;
    private PresentLoot loot;

    private Presents(JavaPlugin plugin) {
        this.plugin = plugin;
        this.presentFallingKey = new NamespacedKey(this.plugin, "fallingPresent");
        this.presentLyingKey = new NamespacedKey(this.plugin, "lyingPresent");
    }

    public static Presents getInstance(JavaPlugin plugin) {
        if(Presents.instance == null) {
            Presents.instance = new Presents(plugin);
            plugin.getServer().getPluginManager().registerEvents(Presents.instance, Presents.instance.plugin);
        }
        return Presents.instance;
    }

    public void setLootGenerator(PresentLoot loot) {
        this.loot = loot;
    }
    
    public void spawn(Location loc) {
        ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
        armorStand.setMarker(false);
        armorStand.setVisible(false);
        armorStand.setInvulnerable(false);
        armorStand.setNoPhysics(false);
        armorStand.setPersistent(true);
        armorStand.customName(Component.text("OPEN ME"));
        armorStand.setCustomNameVisible(true);
        armorStand.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false));
        armorStand.getEquipment().setHelmet(new ItemStack(Material.SKELETON_SKULL));
        armorStand.getPersistentDataContainer().set(this.presentFallingKey, PersistentDataType.BOOLEAN, true);
    }

    @EventHandler
    public void onPresentTaken(EntityDamageEvent event) {
        Entity armorStandEntity = event.getEntity();
        if(!armorStandEntity.getPersistentDataContainer().has(this.presentFallingKey)) {
            return;
        }

        if(!(armorStandEntity instanceof ArmorStand armorStand)) {
            return;
        }

        getServer().broadcast(Component.text("TAKE"));

        ItemStack presentItem = new ItemStack(Material.SKELETON_SKULL);
        presentItem.editPersistentDataContainer(pdc -> {
            pdc.set(this.presentLyingKey, PersistentDataType.BOOLEAN, true);
        });
        armorStand.getWorld().dropItemNaturally(armorStand.getLocation(), presentItem);
        armorStand.remove();
    }

    @EventHandler
    public void onPresentPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        ItemStack item = event.getItemInHand();

        if(!item.getPersistentDataContainer().has(this.presentLyingKey)) {
            return;
        }

        if(!(block.getState() instanceof Skull skull)) {
            return;
        }

        getServer().broadcast(Component.text("PLACE"));

        Bukkit.getScheduler().runTask(this.plugin, () -> {
            skull.getPersistentDataContainer().set(this.presentLyingKey, PersistentDataType.BOOLEAN, true);
            skull.update();
        });
    }

    @EventHandler
    public void onPresentBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if(!(block.getState() instanceof Skull skull)) {
            return;
        }

        if(!skull.getPersistentDataContainer().has(this.presentLyingKey)) {
            return;
        }

        getServer().broadcast(Component.text("BREAK"));

        boolean silkTouch = false;
        if (tool != null && tool.getType() != Material.AIR) {
            if (tool.getItemMeta() != null && tool.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
                silkTouch = true;
            }
        }

        event.setDropItems(false);
        if(silkTouch) {
            ItemStack drop = new ItemStack(block.getType());
            drop.editPersistentDataContainer(pdc -> {
                pdc.set(this.presentLyingKey, PersistentDataType.BOOLEAN, true);
            });
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
        } else {
            PresentLoot.GenerateResult genRes = this.loot.gen();
            ItemStack drop = new ItemStack(genRes.material(), genRes.itemCount());
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
        }
    }
}
