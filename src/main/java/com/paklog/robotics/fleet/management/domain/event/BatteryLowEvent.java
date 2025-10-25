package com.paklog.robotics.fleet.management.domain.event;

import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;

import java.time.Instant;

public class BatteryLowEvent {
    private String robotId;
    private int batteryPercentage;
    private boolean emergency;
    private RobotPosition position;
    private Instant occurredAt;

    public BatteryLowEvent(String robotId, int batteryPercentage, boolean emergency, RobotPosition position, Instant occurredAt) {
        this.robotId = robotId;
        this.batteryPercentage = batteryPercentage;
        this.emergency = emergency;
        this.position = position;
        this.occurredAt = occurredAt;
    }

    public String getRobotId() { return robotId; }
    public int getBatteryPercentage() { return batteryPercentage; }
    public boolean isEmergency() { return emergency; }
    public RobotPosition getPosition() { return position; }
    public Instant getOccurredAt() { return occurredAt; }
}
