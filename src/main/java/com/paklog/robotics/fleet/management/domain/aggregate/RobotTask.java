package com.paklog.robotics.fleet.management.domain.aggregate;

import com.paklog.robotics.fleet.management.domain.valueobject.*;

import java.time.Instant;
import java.util.Map;

/**
 * RobotTask Aggregate
 * Represents a task that can be assigned to a robot
 */
public class RobotTask {

    private String taskId;
    private String robotId;
    private TaskType taskType;
    private TaskPriority priority;
    private RobotPosition origin;
    private RobotPosition destination;
    private Map<String, Object> payload;
    private TaskStatus status;
    private RobotCapability requiredCapability;
    private Instant createdAt;
    private Instant assignedAt;
    private Instant startedAt;
    private Instant completedAt;
    private String failureReason;

    // Private constructor for DDD
    private RobotTask() {
    }

    /**
     * Factory method to create a new task
     */
    public static RobotTask create(String taskId, TaskType taskType, TaskPriority priority,
                                  RobotPosition origin, RobotPosition destination,
                                  RobotCapability requiredCapability,
                                  Map<String, Object> payload) {
        RobotTask task = new RobotTask();
        task.taskId = taskId;
        task.taskType = taskType;
        task.priority = priority;
        task.origin = origin;
        task.destination = destination;
        task.requiredCapability = requiredCapability;
        task.payload = payload;
        task.status = TaskStatus.PENDING;
        task.createdAt = Instant.now();
        return task;
    }

    /**
     * Assign task to a robot
     */
    public void assign(String robotId) {
        if (status != TaskStatus.PENDING) {
            throw new IllegalStateException("Task is not in PENDING status");
        }

        this.robotId = robotId;
        this.status = TaskStatus.ASSIGNED;
        this.assignedAt = Instant.now();
    }

    /**
     * Start task execution
     */
    public void start() {
        if (status != TaskStatus.ASSIGNED) {
            throw new IllegalStateException("Task is not in ASSIGNED status");
        }

        this.status = TaskStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
    }

    /**
     * Complete task
     */
    public void complete() {
        if (status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("Task is not in IN_PROGRESS status");
        }

        this.status = TaskStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    /**
     * Fail task
     */
    public void fail(String reason) {
        if (status == TaskStatus.COMPLETED || status == TaskStatus.CANCELLED) {
            throw new IllegalStateException("Cannot fail a completed or cancelled task");
        }

        this.status = TaskStatus.FAILED;
        this.failureReason = reason;
        this.completedAt = Instant.now();
    }

    /**
     * Cancel task
     */
    public void cancel() {
        if (status == TaskStatus.COMPLETED || status == TaskStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel a completed or failed task");
        }

        this.status = TaskStatus.CANCELLED;
        this.completedAt = Instant.now();
    }

    /**
     * Calculate task duration in seconds
     */
    public long getDurationSeconds() {
        if (startedAt == null || completedAt == null) {
            return 0;
        }
        return completedAt.getEpochSecond() - startedAt.getEpochSecond();
    }

    /**
     * Calculate wait time before assignment in seconds
     */
    public long getWaitTimeSeconds() {
        if (assignedAt == null) {
            return Instant.now().getEpochSecond() - createdAt.getEpochSecond();
        }
        return assignedAt.getEpochSecond() - createdAt.getEpochSecond();
    }

    /**
     * Check if task is completed
     */
    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    /**
     * Check if task is in terminal state
     */
    public boolean isTerminal() {
        return status == TaskStatus.COMPLETED ||
               status == TaskStatus.FAILED ||
               status == TaskStatus.CANCELLED;
    }

    // Getters
    public String getTaskId() {
        return taskId;
    }

    public String getRobotId() {
        return robotId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public RobotPosition getOrigin() {
        return origin;
    }

    public RobotPosition getDestination() {
        return destination;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public RobotCapability getRequiredCapability() {
        return requiredCapability;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
