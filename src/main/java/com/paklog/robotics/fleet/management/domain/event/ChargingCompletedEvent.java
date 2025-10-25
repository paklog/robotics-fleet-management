package com.paklog.robotics.fleet.management.domain.event;

import java.time.Instant;

public class ChargingCompletedEvent {
    private String robotId;
    private int finalBatteryLevel;
    private Instant occurredAt;

    public ChargingCompletedEvent(String robotId, int finalBatteryLevel, Instant occurredAt) {
        this.robotId = robotId;
        this.finalBatteryLevel = finalBatteryLevel;
        this.occurredAt = occurredAt;
    }

    public String getRobotId() { return robotId; }
    public int getFinalBatteryLevel() { return finalBatteryLevel; }
    public Instant getOccurredAt() { return occurredAt; }
}
