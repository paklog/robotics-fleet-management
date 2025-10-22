package com.paklog.robotics.fleet.management.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Path Plan Value Object
 * Represents a planned route with waypoints
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PathPlan implements Serializable {

    private final List<RobotPosition> waypoints;
    private final double totalDistance;
    private final double estimatedTimeSeconds;

    public static PathPlan of(List<RobotPosition> waypoints, double totalDistance, double estimatedTimeSeconds) {
        if (waypoints == null || waypoints.isEmpty()) {
            throw new IllegalArgumentException("Path plan must have at least one waypoint");
        }
        if (totalDistance < 0) {
            throw new IllegalArgumentException("Total distance cannot be negative");
        }
        if (estimatedTimeSeconds < 0) {
            throw new IllegalArgumentException("Estimated time cannot be negative");
        }
        return new PathPlan(new ArrayList<>(waypoints), totalDistance, estimatedTimeSeconds);
    }

    public RobotPosition getStartPosition() {
        return waypoints.get(0);
    }

    public RobotPosition getEndPosition() {
        return waypoints.get(waypoints.size() - 1);
    }

    public int getWaypointCount() {
        return waypoints.size();
    }

    public List<RobotPosition> getWaypoints() {
        return Collections.unmodifiableList(waypoints);
    }

    public boolean isEmpty() {
        return waypoints.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("PathPlan(waypoints=%d, distance=%.2fm, eta=%.1fs)",
            waypoints.size(), totalDistance, estimatedTimeSeconds);
    }
}
