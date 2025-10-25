package com.paklog.robotics.fleet.management.domain.event;

import java.time.Instant;

public class RobotTaskFailedEvent {
    private String robotId;
    private String taskId;
    private String reason;
    private Instant occurredAt;

    public RobotTaskFailedEvent(String robotId, String taskId, String reason, Instant occurredAt) {
        this.robotId = robotId;
        this.taskId = taskId;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }

    public String getRobotId() { return robotId; }
    public String getTaskId() { return taskId; }
    public String getReason() { return reason; }
    public Instant getOccurredAt() { return occurredAt; }
}
