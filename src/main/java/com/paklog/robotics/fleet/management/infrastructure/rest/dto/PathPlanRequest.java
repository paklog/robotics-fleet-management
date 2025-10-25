package com.paklog.robotics.fleet.management.infrastructure.rest.dto;

public record PathPlanRequest(
    double startX,
    double startY,
    double goalX,
    double goalY
) {
    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getGoalX() {
        return goalX;
    }

    public double getGoalY() {
        return goalY;
    }
}
