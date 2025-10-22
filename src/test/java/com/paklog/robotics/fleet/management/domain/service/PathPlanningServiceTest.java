package com.paklog.robotics.fleet.management.domain.service;

import com.paklog.robotics.fleet.management.domain.valueobject.PathPlan;
import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;
import com.paklog.robotics.fleet.management.domain.valueobject.TrafficZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PathPlanningServiceTest {

    private PathPlanningService pathPlanningService;

    @BeforeEach
    void setUp() {
        pathPlanningService = new PathPlanningService();
    }

    @Test
    void shouldCalculatePathSuccessfully() {
        RobotPosition start = RobotPosition.of(0.0, 0.0, 0.0);
        RobotPosition goal = RobotPosition.of(10.0, 10.0, 0.0);
        Set<RobotPosition> obstacles = new HashSet<>();
        List<TrafficZone> trafficZones = new ArrayList<>();

        PathPlan path = pathPlanningService.calculatePath(start, goal, obstacles, trafficZones);

        assertNotNull(path);
        assertTrue(path.getWaypoints().size() >= 2);
        assertEquals(start, path.getStartPosition());
        assertEquals(goal, path.getEndPosition());
        assertTrue(path.getTotalDistance() > 0);
    }

    @Test
    void shouldValidatePathCollisionFree() {
        RobotPosition start = RobotPosition.of(0.0, 0.0, 0.0);
        RobotPosition goal = RobotPosition.of(10.0, 10.0, 0.0);

        PathPlan path = pathPlanningService.calculatePath(start, goal, new HashSet<>(), new ArrayList<>());

        Set<RobotPosition> robotPositions = Set.of(
            RobotPosition.of(50.0, 50.0, 0.0)  // Far away
        );

        assertTrue(pathPlanningService.validatePath(path, robotPositions));
    }

    @Test
    void shouldDetectPathCollision() {
        RobotPosition start = RobotPosition.of(0.0, 0.0, 0.0);
        RobotPosition goal = RobotPosition.of(10.0, 10.0, 0.0);

        PathPlan path = pathPlanningService.calculatePath(start, goal, new HashSet<>(), new ArrayList<>());

        Set<RobotPosition> robotPositions = Set.of(
            RobotPosition.of(5.0, 5.0, 0.0)  // On the path
        );

        assertFalse(pathPlanningService.validatePath(path, robotPositions));
    }
}
