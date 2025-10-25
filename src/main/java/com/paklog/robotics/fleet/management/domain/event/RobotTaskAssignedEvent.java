package com.paklog.robotics.fleet.management.domain.event;

import com.paklog.robotics.fleet.management.domain.valueobject.TaskPriority;
import com.paklog.robotics.fleet.management.domain.valueobject.TaskType;

import java.time.Instant;

public class RobotTaskAssignedEvent {
    private String robotId;
    private String taskId;
    private TaskType taskType;
    private TaskPriority priority;
    private Instant occurredAt;

    public RobotTaskAssignedEvent(String robotId, String taskId, TaskType taskType, TaskPriority priority, Instant occurredAt) {
        this.robotId = robotId;
        this.taskId = taskId;
        this.taskType = taskType;
        this.priority = priority;
        this.occurredAt = occurredAt;
    }

    public String getRobotId() { return robotId; }
    public String getTaskId() { return taskId; }
    public TaskType getTaskType() { return taskType; }
    public TaskPriority getPriority() { return priority; }
    public Instant getOccurredAt() { return occurredAt; }
}
