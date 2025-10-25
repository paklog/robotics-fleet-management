package com.paklog.robotics.fleet.management.domain.event;

import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;

import java.time.Instant;

public class RobotTaskStartedEvent {
    private String robotId;
    private String taskId;
    private RobotPosition position;
    private Instant occurredAt;

    public RobotTaskStartedEvent(String robotId, String taskId, RobotPosition position, Instant occurredAt) {
        this.robotId = robotId;
        this.taskId = taskId;
        this.position = position;
        this.occurredAt = occurredAt;
    }

    public String getRobotId() { return robotId; }
    public String getTaskId() { return taskId; }
    public RobotPosition getPosition() { return position; }
    public Instant getOccurredAt() { return occurredAt; }
}
