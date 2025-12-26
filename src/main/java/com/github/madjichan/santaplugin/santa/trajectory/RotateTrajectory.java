package com.github.madjichan.santaplugin.santa.trajectory;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class RotateTrajectory extends Trajectory {
    private Location currentLoc;
    private Vector targetLoc; // TODO rename to targetPos
    private Vector circleCenter;
    private Vector down;

    public RotateTrajectory(Location startLoc, Vector targetLoc, double stepLength, double rotationRadius) {
        super(stepLength);

        this.currentLoc = startLoc.clone();
        this.targetLoc = targetLoc.clone();

        Vector fromTargetToLoc = this.currentLoc.toVector().clone().subtract(this.targetLoc);
        Vector fromTargetToLDir = fromTargetToLoc.clone().add(this.currentLoc.getDirection());
        this.down = fromTargetToLDir.clone().crossProduct(fromTargetToLoc).normalize();
        Vector radius = this.currentLoc.clone().getDirection().crossProduct(down).normalize().multiply(rotationRadius);
        this.circleCenter = this.currentLoc.clone().toVector().add(radius);
    }

    public Vector getTarget() {
        return this.targetLoc;
    }

    @Override
    public Location nextPoint() {
        Vector aradius = this.circleCenter.clone().subtract(this.currentLoc.toVector());
        Vector direction = this.down.clone().crossProduct(aradius).normalize();
        Vector resPos = this.currentLoc.clone().toVector().add(direction.clone().multiply(this.stepLength));
        Location res = new Location(this.currentLoc.getWorld(), resPos.getX(), resPos.getY(), resPos.getZ());
        res.setDirection(direction);

        Vector resDirPos = resPos.clone().add(res.getDirection());
        Vector rPos = resPos.clone().subtract(this.targetLoc);
        Vector rDir = resDirPos.clone().subtract(this.targetLoc);
        Vector currentDown = rDir.clone().crossProduct(rPos);
        double dotPr = this.down.dot(currentDown);
        if(dotPr < 0.0) {
            res = null;
        } else {
            this.currentLoc = res.clone();
        }
        return res;
    }
}
