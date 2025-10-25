package com.paklog.robotics.fleet.management.domain.event;

import java.time.Instant;

public class FleetRebalancedEvent {
    private String fleetId;
    private int robotCount;
    private double utilizationRate;
    private Instant occurredAt;

    public FleetRebalancedEvent(String fleetId, int robotCount, double utilizationRate, Instant occurredAt) {
        this.fleetId = fleetId;
        this.robotCount = robotCount;
        this.utilizationRate = utilizationRate;
        this.occurredAt = occurredAt;
    }

    public String getFleetId() { return fleetId; }
    public int getRobotCount() { return robotCount; }
    public double getUtilizationRate() { return utilizationRate; }
    public Instant getOccurredAt() { return occurredAt; }
}
