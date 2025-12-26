package com.github.madjichan.santaplugin.santa.trajectory;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class SquareTrajectory extends Trajectory {
    private Location currentLoc;
    private final double rotationRadius;
    private double minX, minZ, maxX, maxZ;
    private double flyHeight;
    private Trajectory state;
    private int currentTargetSide; // MIN_X(0), MAX_X(1), MIN_Z(2), MAX_Z(3);

    public SquareTrajectory(Location startLoc, Location leftCorner, Location rightCorner, double flyHeight, double stepLength, double rotationRadius) {
        super(stepLength);

        this.currentLoc = startLoc;
        this.rotationRadius = rotationRadius;
        this.flyHeight = flyHeight;

        this.minX = Math.min(leftCorner.getX(), rightCorner.getX());
        this.minZ = Math.min(leftCorner.getZ(), rightCorner.getZ());
        this.maxX = Math.max(leftCorner.getX(), rightCorner.getX());
        this.maxZ = Math.max(leftCorner.getZ(), rightCorner.getZ());

        Vector nextTarget = genRandomNextTarget(true);
        this.state = new LineTrajectory(startLoc.getWorld(), startLoc.toVector(), nextTarget, this.stepLength);
    }

    private Vector genRandomNextTarget(boolean isStart) {
        Random rand = new Random();
        int sideCount = 4;

        int nextSide;
        if(!isStart) {
            int offset = (int)(Integer.toUnsignedLong(rand.nextInt()) % (sideCount - 1));
            nextSide = (this.currentTargetSide + offset + 1) % sideCount;
        } else {
            nextSide = (int)(Integer.toUnsignedLong(rand.nextInt()) % sideCount);
        }

        getServer().broadcast(Component.text( "Next Side: " + Integer.valueOf(nextSide).toString()));
        this.currentTargetSide = nextSide;

        double opMin = 0.0, opMax = 0.0, res = 0.0;
        switch(nextSide) {
            case 0: // MIN_X
            case 1: // MAX_X
                opMin = this.minZ;
                opMax = this.maxZ;
                break;
            case 2: // MIN_Z
            case 3: // MAX_Z
                opMin = this.minX;
                opMax = this.maxX;
                break;
        }

        opMin += this.rotationRadius + 1;
        opMax -= this.rotationRadius + 1;

        res = rand.nextDouble() * (opMax - opMin) + opMin;

        switch(nextSide) {
            case 0: // MIN_X
                return new Vector(this.minX, this.flyHeight, res);
            case 1: // MAX_X
                return new Vector(this.maxX, this.flyHeight, res);
            case 2: // MIN_Z
                return new Vector(res, this.flyHeight, this.minZ);
            case 3: // MAX_Z
                return new Vector(res, this.flyHeight, this.maxZ);
        }

        return new Vector(0.0, 0.0, 0.0);
    }

    @Override
    public Location nextPoint() {
        Location res = null;
        if(this.state instanceof LineTrajectory lineTrajectory) {
            Location maybe_res = lineTrajectory.nextPoint();
            if (maybe_res == null) {
                getServer().broadcast(Component.text("ROTATE!"));
                Vector nextTarget = genRandomNextTarget(false);
                this.currentLoc.setPitch(0.0f);
                this.state = new RotateTrajectory(this.currentLoc, nextTarget, this.stepLength, this.rotationRadius);
                res = this.state.nextPoint();
            } else {
                res = maybe_res;
            }
        } else if(this.state instanceof RotateTrajectory rotateTrajectory) {
            Location maybe_res = rotateTrajectory.nextPoint();
            if (maybe_res == null) {
                getServer().broadcast(Component.text("LINE!"));
                Vector nextTarget = rotateTrajectory.getTarget();
                this.state = new LineTrajectory(this.currentLoc.getWorld(), this.currentLoc.toVector(), nextTarget, this.stepLength);
                res = this.state.nextPoint();
            } else {
                res = maybe_res;
            }
        }

        this.currentLoc = res;
        return res.clone();
    }
}
