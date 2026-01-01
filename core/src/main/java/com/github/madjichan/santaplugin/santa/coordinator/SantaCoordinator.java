package com.github.madjichan.santaplugin.santa.coordinator;

import com.github.madjichan.santaplugin.santa.trajectory.Trajectory;
import com.github.madjichan.santaplugin.util.CyclicBuffer;
import com.github.madjichan.santaplugin.util.DoMath;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class SantaCoordinator implements Runnable {
    public interface CoordinatorSlave {
        void setVelocity(Vector strength);
        Location getLocation();
        void startEvent();
        void stopEvent();
    }

    private record RelatedPosition(Vector delta, int tickDelta) {}
    private record PointCacheRecord(Location loc) {}

    private final JavaPlugin plugin;
    private BukkitTask task;
    private Map<CoordinatorSlave, RelatedPosition> listeners;
    private Trajectory trajectory;
    private CyclicBuffer<PointCacheRecord> pointCache;

    public SantaCoordinator(JavaPlugin plugin) {
        this.plugin = plugin;
        this.listeners = new HashMap<>();
    }

    public void addComponent(CoordinatorSlave component, double dx, double dy) {
        if(this.task != null) {
            return;
        }

        Vector delta = new Vector(dx, 0.0, dy);
        this.listeners.put(component, new RelatedPosition(delta, 0));
    }

    public void start(Trajectory trajectory) {
        this.trajectory = trajectory;

        for(var listenerEntry: this.listeners.entrySet()) {
            CoordinatorSlave slave = listenerEntry.getKey();
            RelatedPosition relPos = listenerEntry.getValue();
            int tickDelta = (int)(slave.getLocation().getY() / this.trajectory.getStepLength());
        }

        int mx = 0;
        for(RelatedPosition pos: this.listeners.values()) {
            mx = Math.max(mx, pos.tickDelta + 1);
        }
        this.pointCache = new CyclicBuffer<>(mx);

        for(int i=0; i<this.pointCache.size(); i++) {
            Location nextLoc = this.trajectory.nextPoint();
            this.pointCache.set(i, new PointCacheRecord(nextLoc));
        }

        if(task != null) {
            this.stop();
        }

        for(var listeners: this.listeners.keySet()) {
            listeners.startEvent();
        }

        this.task = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this, 0, 0);
    }

    public void stop() {
        if(task == null) {
            return;
        }

        for(var listeners: this.listeners.keySet()) {
            listeners.stopEvent();
        }
        this.plugin.getServer().getScheduler().cancelTask(this.task.getTaskId());
        this.task = null;
    }

    @Override
    public void run() {
        Location nextLoc = this.trajectory.nextPoint();
        if(nextLoc == null) {
            this.stop();
            return;
        }
        this.pointCache.setNext(new PointCacheRecord(nextLoc));

        for(var listener: this.listeners.entrySet()) {
            CoordinatorSlave coordSlave = listener.getKey();
            RelatedPosition relatedPosition = listener.getValue();

            Location mainPointLoc = this.pointCache.get(relatedPosition.tickDelta).loc;
            Vector mainPointPosition = new Vector(mainPointLoc.getX(), mainPointLoc.getY(), mainPointLoc.getZ());
            Vector mainPointDirection = mainPointLoc.getDirection();
            Vector delta = relatedPosition.delta;

            Vector rotatedRelatedPosition = DoMath.rotateByDirection(delta, mainPointDirection);
            Vector nextPosition = mainPointPosition.add(rotatedRelatedPosition);

            Location currentLoc = coordSlave.getLocation();
            Vector currentPosition = new Vector(currentLoc.getX(), currentLoc.getY(), currentLoc.getZ());
            Vector velocityDelta = nextPosition.subtract(currentPosition);

            coordSlave.setVelocity(velocityDelta);
        }
    }
}
