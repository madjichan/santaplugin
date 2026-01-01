package com.github.madjichan.santaplugin.santa.trajectory;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class LineTrajectory extends Trajectory {
    private World world;
    private Vector currentLoc;
    private Vector targetLoc;

    public LineTrajectory(World world, Vector startLoc, Vector targetLoc, double stepLength) {
        super(stepLength);

        this.world = world;
        this.currentLoc = startLoc.clone();
        this.targetLoc = targetLoc.clone();
    }

    @Override
    public Location nextPoint() {
        Vector prevPos = this.currentLoc.clone();
        Vector direction = this.targetLoc.clone().subtract(this.currentLoc).normalize().multiply(this.stepLength);
        // direction = new Vector(0.0, 0.0, -this.stepLength);
        this.currentLoc.add(direction);
        Location res = new Location(this.world, this.currentLoc.getX(),
                this.currentLoc.getY(),
                this.currentLoc.getZ()
        );
        res.setDirection(direction);

        Vector toCurrent = this.targetLoc.clone().subtract(prevPos);
        Vector toPrev = this.targetLoc.clone().subtract(this.currentLoc);
        if(toCurrent.dot(toPrev) < 0.0) {
            res = null;
        }
        return res;
    }
}
