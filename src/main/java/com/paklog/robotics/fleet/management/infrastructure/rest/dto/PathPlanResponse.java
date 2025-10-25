package com.paklog.robotics.fleet.management.infrastructure.rest.dto;

public record PathPlanResponse(
    int waypointCount,
    double totalDistance,
    double estimatedTimeSeconds
) {}
