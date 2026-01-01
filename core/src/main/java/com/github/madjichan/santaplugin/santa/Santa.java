package com.github.madjichan.santaplugin.santa;

import com.github.madjichan.santaplugin.santa.coordinator.SantaCoordinator;
import com.github.madjichan.santaplugin.santa.trajectory.Trajectory;
import com.github.madjichan.santaplugin.santa.entity.SantaEntity;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Santa {
    private SantaEntity santaEnt;
    private SantaCoordinator coordinator;

    Santa() {

    }

    public static Santa spawn(JavaPlugin plugin, Location loc) {
        Santa n_santa = new Santa();
        n_santa.santaEnt = SantaEntity.spawn(plugin, loc);
        n_santa.coordinator = new SantaCoordinator(plugin);
        n_santa.santaEnt.subscribe(n_santa.coordinator);
        return n_santa;
    }

    public void start(Trajectory trajectory) {
        this.coordinator.start(trajectory);
    }

    public void stop() {
        this.coordinator.stop();
    }
}
