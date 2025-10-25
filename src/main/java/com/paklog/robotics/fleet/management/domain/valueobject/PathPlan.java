package com.paklog.robotics.fleet.management.domain.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Path Plan Value Object
 * Represents a planned route with waypoints
 */
public record PathPlan(
    List<RobotPosition> waypoints,
    double totalDistance,
    double estimatedTimeSeconds
) implements Serializable {

    public int getWaypointCount() {
        return waypoints != null ? waypoints.size() : 0;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getEstimatedTimeSeconds() {
        return estimatedTimeSeconds;
    }

    public static PathPlan of(List<RobotPosition> waypoints, double totalDistance, double estimatedTimeSeconds) {
        return new PathPlan(
            waypoints != null ? Collections.unmodifiableList(new ArrayList<>(waypoints)) : Collections.emptyList(),
            totalDistance,
            estimatedTimeSeconds
        );
    }
}
