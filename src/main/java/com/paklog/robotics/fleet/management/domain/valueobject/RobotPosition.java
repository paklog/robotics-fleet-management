package com.paklog.robotics.fleet.management.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * Robot Position Value Object
 * Represents robot location in warehouse grid with heading
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RobotPosition implements Serializable {

    private final double x;
    private final double y;
    private final double heading; // in degrees (0-360)

    public static RobotPosition of(double x, double y, double heading) {
        validateCoordinates(x, y);
        validateHeading(heading);
        return new RobotPosition(x, y, heading);
    }

    private static void validateCoordinates(double x, double y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates cannot be negative");
        }
        if (x > 10000 || y > 10000) {
            throw new IllegalArgumentException("Coordinates exceed warehouse bounds");
        }
    }

    private static void validateHeading(double heading) {
        if (heading < 0 || heading >= 360) {
            throw new IllegalArgumentException("Heading must be between 0 and 360 degrees");
        }
    }

    /**
     * Calculate Euclidean distance to another position
     */
    public double distanceTo(RobotPosition other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Check if this position is within safety margin of another position
     */
    public boolean isTooCloseTo(RobotPosition other, double safetyMargin) {
        return distanceTo(other) < safetyMargin;
    }

    @Override
    public String toString() {
        return String.format("Position(x=%.2f, y=%.2f, heading=%.1f°)", x, y, heading);
    }
}
