package com.github.madjichan.santaplugin.santa.entity;

import com.github.madjichan.santaplugin.config.SantaConfiguration;
import com.github.madjichan.santaplugin.present.PresentSenderComponent;
import com.github.madjichan.santaplugin.santa.coordinator.SantaCoordinator;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SantaEntity implements Listener {
    public record SantaComponent(String name, EntityType ent, double dx, double dy) {}

    private SantaEntityComponent[] components;
    private PresentSenderComponent presentSender;
    private Location loc;

    public SantaEntity() {
    }

    static public SantaEntity spawn(JavaPlugin plugin, Location loc) {
        SantaEntity n_ent = new SantaEntity();
//        n_ent.firstHorseEnt = SantaEntityComponent.spawn(loc, EntityType.HORSE, "FiRsT");
//        n_ent.secondHorseEnt = SantaEntityComponent.spawn(loc, EntityType.HORSE, "SeCoNd");
//        n_ent.thirdHorseEnt = SantaEntityComponent.spawn(loc, EntityType.HORSE, "ThIrD");
//        n_ent.fourthHorseEnt = SantaEntityComponent.spawn(loc, EntityType.HORSE, "FoUrTh");

        SantaConfiguration config = SantaConfiguration.getInstance();
        n_ent.components = new SantaEntityComponent[config.santaComponents.length];
        for(int i=0; i<config.santaComponents.length; i++) {
            SantaComponent configData = config.santaComponents[i];
            n_ent.components[i] = SantaEntityComponent.spawn(loc, configData.ent, configData.name);
        }

        n_ent.presentSender = PresentSenderComponent.spawn(plugin, loc, (int)(SantaConfiguration.getInstance().presentDropPeriod * 20.0));

        return n_ent;
    }

    public void subscribe(SantaCoordinator coordinator) {
        SantaConfiguration config = SantaConfiguration.getInstance();
        for(int i=0; i<config.santaComponents.length; i++) {
            SantaComponent configData = config.santaComponents[i];
            coordinator.addComponent(this.components[i], configData.dx, configData.dy);
        }
        coordinator.addComponent(presentSender, config.marker.dx, config.marker.dy);

//        coordinator.addComponent(firstHorseEnt, 1.0, 0.0);
//        coordinator.addComponent(secondHorseEnt, -1.0, 0.0);
//        coordinator.addComponent(thirdHorseEnt, 1.0, 1.0);
//        coordinator.addComponent(fourthHorseEnt, -1.0, 1.0);

//        coordinator.addComponent(presentSender, 0.0, 3.0);
    }
}
