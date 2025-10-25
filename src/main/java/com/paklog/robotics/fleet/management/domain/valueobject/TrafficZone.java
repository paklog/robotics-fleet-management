package com.paklog.robotics.fleet.management.domain.valueobject;


import java.io.Serializable;

/**
 * Traffic Zone Value Object
 * Represents a spatial area with movement rules
 */
public class TrafficZone implements Serializable {

    private final String zoneId;
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private final int maxRobotsAllowed;
    private final double speedLimit; // meters per second
    private final TrafficZoneType type;

    public static TrafficZone of(String zoneId, double minX, double minY, double maxX, double maxY,
                                 int maxRobotsAllowed, double speedLimit, TrafficZoneType type) {
        if (minX >= maxX || minY >= maxY) {
            throw new IllegalArgumentException("Invalid zone boundaries");
        }
        if (maxRobotsAllowed <= 0) {
            throw new IllegalArgumentException("Max robots allowed must be positive");
        }
        if (speedLimit <= 0) {
            throw new IllegalArgumentException("Speed limit must be positive");
        }
        return new TrafficZone(zoneId, minX, minY, maxX, maxY, maxRobotsAllowed, speedLimit, type);
    }

    /**
     * Check if a position is within this zone
     */

    public TrafficZone(
        String zoneId,
        double minX,
        double minY,
        double maxX,
        double maxY,
        int maxRobotsAllowed,
        double speedLimit,
        TrafficZoneType type
    ) {
        this.zoneId = zoneId;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxRobotsAllowed = maxRobotsAllowed;
        this.speedLimit = speedLimit;
        this.type = type;
    }

    public boolean contains(RobotPosition position) {
        return position.getX() >= minX && position.getX() <= maxX &&
               position.getY() >= minY && position.getY() <= maxY;
    }

    /**
     * Check if this zone overlaps with another zone
     */
    public boolean overlaps(TrafficZone other) {
        return !(this.maxX < other.minX || this.minX > other.maxX ||
                 this.maxY < other.minY || this.minY > other.maxY);
    }

    public double getArea() {
        return (maxX - minX) * (maxY - minY);
    }

    @Override
    public String toString() {
        return String.format("Zone(%s, %s, capacity=%d, limit=%.1fm/s)",
            zoneId, type, maxRobotsAllowed, speedLimit);
    }
}
