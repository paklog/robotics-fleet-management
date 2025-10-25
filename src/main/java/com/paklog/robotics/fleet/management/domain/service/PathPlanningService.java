package com.paklog.robotics.fleet.management.domain.service;

import com.paklog.robotics.fleet.management.domain.valueobject.PathPlan;
import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;
import com.paklog.robotics.fleet.management.domain.valueobject.TrafficZone;

import java.util.List;
import java.util.Set;

/**
 * Path Planning Service Interface
 * Responsible for calculating optimal paths for robots
 */
public interface PathPlanningService {

    /**
     * Calculate optimal path from start to goal position
     * @param start Starting position
     * @param goal Goal position
     * @param blockedZones Set of zones that are currently blocked
     * @param trafficZones List of high-traffic zones to consider
     * @return Calculated path plan
     */
    PathPlan calculatePath(
        RobotPosition start,
        RobotPosition goal,
        Set<RobotPosition> blockedZones,
        List<TrafficZone> trafficZones
    );

    /**
     * Validate if a path is safe and feasible
     * @param path The path to validate
     * @param blockedZones Set of zones that are currently blocked
     * @return true if path is valid, false otherwise
     */
    boolean validatePath(PathPlan path, Set<RobotPosition> blockedZones);

    /**
     * Recalculate path avoiding specific positions
     * @param currentPath Current path being followed
     * @param currentPosition Current robot position
     * @param positionsToAvoid Positions that should be avoided
     * @return Recalculated path plan
     */
    PathPlan recalculatePath(
        PathPlan currentPath,
        RobotPosition currentPosition,
        Set<RobotPosition> positionsToAvoid
    );
}
