package com.paklog.robotics.fleet.management.infrastructure.rest.controller;

import com.paklog.robotics.fleet.management.domain.service.PathPlanningService;
import com.paklog.robotics.fleet.management.domain.valueobject.PathPlan;
import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;
import com.paklog.robotics.fleet.management.infrastructure.rest.dto.PathPlanRequest;
import com.paklog.robotics.fleet.management.infrastructure.rest.dto.PathPlanResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/paths")
public class PathController {

    private final PathPlanningService pathPlanningService;
    public PathController(PathPlanningService pathPlanningService) {
        this.pathPlanningService = pathPlanningService;
    }


    @PostMapping("/calculate")
    public ResponseEntity<PathPlanResponse> calculatePath(@RequestBody PathPlanRequest request) {
        RobotPosition start = RobotPosition.of(
            request.getStartX(),
            request.getStartY(),
            0.0
        );

        RobotPosition goal = RobotPosition.of(
            request.getGoalX(),
            request.getGoalY(),
            0.0
        );

        PathPlan path = pathPlanningService.calculatePath(
            start,
            goal,
            new HashSet<>(),
            new ArrayList<>()
        );

        PathPlanResponse response = new PathPlanResponse(
            path.getWaypointCount(),
            path.getTotalDistance(),
            path.getEstimatedTimeSeconds()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validatePath(@RequestBody PathPlanRequest request) {
        RobotPosition start = RobotPosition.of(request.getStartX(), request.getStartY(), 0.0);
        RobotPosition goal = RobotPosition.of(request.getGoalX(), request.getGoalY(), 0.0);

        PathPlan path = pathPlanningService.calculatePath(start, goal, new HashSet<>(), new ArrayList<>());
        boolean isValid = pathPlanningService.validatePath(path, new HashSet<>());

        return ResponseEntity.ok(Map.of("isValid", isValid));
    }
}
