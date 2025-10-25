package com.paklog.robotics.fleet.management.domain.event;

import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;

import java.time.Instant;

public class ChargingStartedEvent {
    private String robotId;
    private int currentBatteryLevel;
    private RobotPosition position;
    private Instant occurredAt;

    public ChargingStartedEvent(String robotId, int currentBatteryLevel, RobotPosition position, Instant occurredAt) {
        this.robotId = robotId;
        this.currentBatteryLevel = currentBatteryLevel;
        this.position = position;
        this.occurredAt = occurredAt;
    }

    public String getRobotId() { return robotId; }
    public int getCurrentBatteryLevel() { return currentBatteryLevel; }
    public RobotPosition getPosition() { return position; }
    public Instant getOccurredAt() { return occurredAt; }
}
