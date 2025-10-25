package com.paklog.robotics.fleet.management.domain.event;

import java.time.Instant;
import java.util.Map;

public class RobotMaintenanceRequiredEvent {
    private String robotId;
    private Map<String, Object> healthMetrics;
    private Instant occurredAt;

    public RobotMaintenanceRequiredEvent(String robotId, Map<String, Object> healthMetrics, Instant occurredAt) {
        this.robotId = robotId;
        this.healthMetrics = healthMetrics;
        this.occurredAt = occurredAt;
    }

    public String getRobotId() { return robotId; }
    public Map<String, Object> getHealthMetrics() { return healthMetrics; }
    public Instant getOccurredAt() { return occurredAt; }
}
