package com.github.madjichan.santaplugin.present;

import com.github.madjichan.santaplugin.santa.coordinator.SantaCoordinator;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import static org.bukkit.Bukkit.getServer;

public class PresentSenderComponent implements SantaCoordinator.CoordinatorSlave, Runnable {
    private ArmorStand marker;
    private JavaPlugin plugin;
    private BukkitTask task;
    private int period;

    private PresentSenderComponent(JavaPlugin plugin, int period) {
        this.plugin = plugin;
        this.period = period;
    }

    public static PresentSenderComponent spawn(JavaPlugin plugin, Location loc, int period) {
        PresentSenderComponent n_sender = new PresentSenderComponent(plugin, period);

        n_sender.marker = loc.getWorld().spawn(loc, ArmorStand.class);
        n_sender.marker.setMarker(true);
        n_sender.marker.setVisible(false);
        n_sender.marker.setGravity(false);
        n_sender.marker.setInvulnerable(true);
        n_sender.marker.setPersistent(false);

        return n_sender;
    }

    @Override
    public void startEvent() {
        this.plugin.getServer().broadcast(Component.text("Sender has been started"));
        this.task = this.plugin.getServer().getScheduler().runTaskTimer(plugin, this, 1, this.period);
    }

    @Override
    public void stopEvent() {
        this.plugin.getServer().broadcast(Component.text("Sender has been stopped"));
        this.plugin.getServer().getScheduler().cancelTask(this.task.getTaskId());
        this.task = null;
    }

    @Override
    public void setVelocity(Vector strength) {
        Vector nextPosition = this.marker.getLocation().toVector().clone().add(strength);
        this.marker.teleport(new Location(this.marker.getWorld(), nextPosition.getX(), nextPosition.getY(), nextPosition.getZ()));
    }

    @Override
    public Location getLocation() {
        return this.marker.getLocation();
    }

    @Override
    public void run() {
        this.plugin.getServer().broadcast(Component.text("Sender sends present"));
        Presents presents = Presents.getInstance(null);
        presents.spawn(this.getLocation());
    }
}
