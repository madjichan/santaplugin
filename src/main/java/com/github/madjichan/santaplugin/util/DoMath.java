package com.github.madjichan.santaplugin.util;

import org.bukkit.util.Vector;

public class DoMath {
    public static Vector rotateByDirection(Vector local, Vector forward) {
        forward = forward.clone().normalize();

        // World up (Y+)
        Vector worldUp = new Vector(0, 1, 0);

        // Right = forward × worldUp
        Vector right = forward.clone().crossProduct(worldUp).normalize();

        // Up = right × forward
        Vector up = right.clone().crossProduct(forward).normalize();

        // Transform local vector, invert Z to match Minecraft forward convention
        Vector world = right.clone().multiply(local.getX())
                .add(up.clone().multiply(local.getY()))
                .add(forward.clone().multiply(-local.getZ())); // <-- Z inversion

        return world;
    }
}
