package com.github.madjichan.santaplugin.santa.entity;

import com.github.madjichan.santaplugin.santa.coordinator.SantaCoordinator;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;

public class SantaEntityComponent implements SantaCoordinator.CoordinatorSlave {
    private Entity ent;

    private SantaEntityComponent() {

    }

    static SantaEntityComponent spawn(Location loc, EntityType entType, String label) {
        SantaEntityComponent n_comp = new SantaEntityComponent();
        n_comp.ent = loc.getWorld().spawnEntity(loc, entType);
        n_comp.ent.setGravity(false);
        n_comp.ent.setInvulnerable(true);
        n_comp.ent.setPersistent(false);
        if(n_comp.ent instanceof Mob mob) {
            mob.setAware(false);
        }
        n_comp.ent.customName(Component.text(label));
        n_comp.ent.setCustomNameVisible(true);
        return n_comp;
    }

    @Override
    public void setVelocity(Vector strength) {
        this.ent.setVelocity(strength);

        Location tmp = new Location(this.ent.getWorld(), 0.0, 0.0, 0.0);
        tmp.setDirection(strength);
        this.ent.setRotation(tmp.getYaw(), tmp.getPitch());
    }

    @Override
    public Location getLocation() {
        return this.ent.getLocation();
    }

    @Override
    public void startEvent() {

    }

    @Override
    public void stopEvent() {

    }
}
