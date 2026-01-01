package com.github.madjichan.santaplugin.santa.trajectory;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class Trajectory {
    protected final double stepLength;

    public Trajectory(double stepLength) {
        this.stepLength = stepLength;
    }

    public double getStepLength() {
        return this.stepLength;
    }

    public abstract Location nextPoint();
}
