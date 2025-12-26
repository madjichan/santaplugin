package com.github.madjichan.santaplugin.santa.coordinator;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface CoordinatorSlave {
    void setVelocity(Vector strength);
    Location getLocation();
    void startEvent();
    void stopEvent();
}
