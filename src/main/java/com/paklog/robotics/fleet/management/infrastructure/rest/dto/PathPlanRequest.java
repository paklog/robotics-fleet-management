package com.paklog.robotics.fleet.management.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PathPlanRequest {
    private double startX;
    private double startY;
    private double goalX;
    private double goalY;
}
