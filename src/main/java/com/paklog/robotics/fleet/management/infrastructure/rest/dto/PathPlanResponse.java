package com.paklog.robotics.fleet.management.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathPlanResponse {
    private int waypointCount;
    private double totalDistance;
    private double estimatedTimeSeconds;
}
