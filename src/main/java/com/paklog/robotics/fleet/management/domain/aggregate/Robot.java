package com.paklog.robotics.fleet.management.domain.aggregate;

import com.paklog.robotics.fleet.management.domain.event.*;
import com.paklog.robotics.fleet.management.domain.valueobject.*;

import java.time.Instant;
import java.util.*;

/**
 * Robot Aggregate Root
 * Manages robot lifecycle, task execution, and health monitoring
 */
public class Robot {

    private RobotId robotId;
    private String model;
    private RobotStatus status;
    private RobotPosition position;
    private BatteryLevel batteryLevel;
    private String currentTaskId;
    private Set<RobotCapability> capabilities;
    private Map<String, Object> healthMetrics;
    private Instant lastHeartbeat;
    private Instant createdAt;
    private Instant updatedAt;

    // Domain events
    private final List<Object> domainEvents = new ArrayList<>();

    // Private constructor for DDD
    private Robot() {
        this.healthMetrics = new HashMap<>();
        this.capabilities = new HashSet<>();
    }

    /**
     * Factory method to register a new robot
     */
    public static Robot register(RobotId robotId, String model, RobotPosition initialPosition,
                                 Set<RobotCapability> capabilities) {
        Robot robot = new Robot();
        robot.robotId = robotId;
        robot.model = model;
        robot.position = initialPosition;
        robot.capabilities = new HashSet<>(capabilities);
        robot.status = RobotStatus.IDLE;
        robot.batteryLevel = BatteryLevel.of(100); // Start with full battery
        robot.createdAt = Instant.now();
        robot.updatedAt = Instant.now();
        robot.lastHeartbeat = Instant.now();

        robot.addDomainEvent(new RobotRegisteredEvent(
            robotId.getValue(),
            model,
            initialPosition,
            capabilities,
            Instant.now()
        ));

        return robot;
    }

    /**
     * Assign a task to this robot
     */
    public void assignTask(RobotTask task) {
        validateCanAcceptTask(task);

        this.currentTaskId = task.getTaskId();
        this.status = RobotStatus.EXECUTING;
        this.updatedAt = Instant.now();

        addDomainEvent(new RobotTaskAssignedEvent(
            robotId.getValue(),
            task.getTaskId(),
            task.getTaskType(),
            task.getPriority(),
            Instant.now()
        ));
    }

    private void validateCanAcceptTask(RobotTask task) {
        if (status != RobotStatus.IDLE) {
            throw new IllegalStateException("Robot is not idle, current status: " + status);
        }

        if (!batteryLevel.isSufficientForTask()) {
            throw new IllegalStateException(
                String.format("Insufficient battery level (%d%%) for task assignment",
                    batteryLevel.getPercentage())
            );
        }

        if (!hasRequiredCapability(task.getRequiredCapability())) {
            throw new IllegalStateException(
                String.format("Robot lacks required capability: %s", task.getRequiredCapability())
            );
        }
    }

    /**
     * Start task execution
     */
    public void startTask() {
        if (status != RobotStatus.EXECUTING) {
            throw new IllegalStateException("Cannot start task, robot is not in EXECUTING status");
        }

        if (currentTaskId == null) {
            throw new IllegalStateException("No task assigned to robot");
        }

        this.updatedAt = Instant.now();

        addDomainEvent(new RobotTaskStartedEvent(
            robotId.getValue(),
            currentTaskId,
            position,
            Instant.now()
        ));
    }

    /**
     * Complete current task
     */
    public void completeTask() {
        if (currentTaskId == null) {
            throw new IllegalStateException("No task to complete");
        }

        String completedTaskId = this.currentTaskId;
        this.currentTaskId = null;
        this.status = RobotStatus.IDLE;
        this.updatedAt = Instant.now();

        addDomainEvent(new RobotTaskCompletedEvent(
            robotId.getValue(),
            completedTaskId,
            position,
            Instant.now()
        ));
    }

    /**
     * Fail current task
     */
    public void failTask(String reason) {
        if (currentTaskId == null) {
            throw new IllegalStateException("No task to fail");
        }

        String failedTaskId = this.currentTaskId;
        this.currentTaskId = null;
        this.status = RobotStatus.ERROR;
        this.updatedAt = Instant.now();

        addDomainEvent(new RobotTaskFailedEvent(
            robotId.getValue(),
            failedTaskId,
            reason,
            Instant.now()
        ));
    }

    /**
     * Check battery level and trigger low battery event if needed
     */
    public void checkBatteryLevel() {
        if (batteryLevel.needsEmergencyCharging()) {
            addDomainEvent(new BatteryLowEvent(
                robotId.getValue(),
                batteryLevel.getPercentage(),
                true, // emergency
                position,
                Instant.now()
            ));
        } else if (batteryLevel.needsCharging()) {
            addDomainEvent(new BatteryLowEvent(
                robotId.getValue(),
                batteryLevel.getPercentage(),
                false, // not emergency
                position,
                Instant.now()
            ));
        }
    }

    /**
     * Send robot to charging station
     */
    public void sendToCharging() {
        if (status == RobotStatus.CHARGING) {
            throw new IllegalStateException("Robot is already charging");
        }

        if (currentTaskId != null) {
            // Cancel current task if battery is critical
            if (batteryLevel.needsEmergencyCharging()) {
                failTask("Emergency charging required");
            } else {
                throw new IllegalStateException("Cannot charge while task is in progress");
            }
        }

        this.status = RobotStatus.CHARGING;
        this.updatedAt = Instant.now();

        addDomainEvent(new ChargingStartedEvent(
            robotId.getValue(),
            batteryLevel.getPercentage(),
            position,
            Instant.now()
        ));
    }

    /**
     * Complete charging process
     */
    public void completeCharging() {
        if (status != RobotStatus.CHARGING) {
            throw new IllegalStateException("Robot is not in charging status");
        }

        this.batteryLevel = BatteryLevel.of(100);
        this.status = RobotStatus.IDLE;
        this.updatedAt = Instant.now();

        addDomainEvent(new ChargingCompletedEvent(
            robotId.getValue(),
            100,
            Instant.now()
        ));
    }

    /**
     * Update robot position
     */
    public void updatePosition(RobotPosition newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        this.position = newPosition;
        this.updatedAt = Instant.now();
        this.lastHeartbeat = Instant.now();
    }

    /**
     * Update battery level
     */
    public void updateBatteryLevel(int percentage) {
        this.batteryLevel = BatteryLevel.of(percentage);
        this.updatedAt = Instant.now();
        checkBatteryLevel();
    }

    /**
     * Perform health check
     */
    public void performHealthCheck() {
        this.lastHeartbeat = Instant.now();

        // Check if robot needs maintenance based on health metrics
        if (needsMaintenance()) {
            this.status = RobotStatus.MAINTENANCE;
            addDomainEvent(new RobotMaintenanceRequiredEvent(
                robotId.getValue(),
                healthMetrics,
                Instant.now()
            ));
        }
    }

    /**
     * Update health metrics
     */
    public void updateHealthMetrics(Map<String, Object> metrics) {
        this.healthMetrics.putAll(metrics);
        this.updatedAt = Instant.now();
    }

    /**
     * Check if robot has required capability
     */
    public boolean hasRequiredCapability(RobotCapability required) {
        return capabilities.contains(required);
    }

    /**
     * Check if robot is available for task assignment
     */
    public boolean isAvailable() {
        return status == RobotStatus.IDLE &&
               batteryLevel.isSufficientForTask() &&
               isHealthy();
    }

    /**
     * Check if robot is healthy
     */
    public boolean isHealthy() {
        return status != RobotStatus.ERROR &&
               status != RobotStatus.MAINTENANCE &&
               status != RobotStatus.OFFLINE;
    }

    /**
     * Check if robot needs maintenance
     */
    private boolean needsMaintenance() {
        // Simple heuristic - can be enhanced with ML models
        Integer errorCount = (Integer) healthMetrics.getOrDefault("errorCount", 0);
        Double temperature = (Double) healthMetrics.getOrDefault("temperature", 0.0);

        return errorCount > 10 || temperature > 80.0;
    }

    /**
     * Mark robot as offline
     */
    public void markOffline() {
        this.status = RobotStatus.OFFLINE;
        this.updatedAt = Instant.now();
    }

    /**
     * Mark robot as online
     */
    public void markOnline() {
        if (status == RobotStatus.OFFLINE) {
            this.status = RobotStatus.IDLE;
            this.updatedAt = Instant.now();
            this.lastHeartbeat = Instant.now();
        }
    }

    /**
     * Calculate distance to a target position
     */
    public double distanceTo(RobotPosition target) {
        return this.position.distanceTo(target);
    }

    /**
     * Add domain event
     */
    private void addDomainEvent(Object event) {
        this.domainEvents.add(event);
    }

    /**
     * Get and clear domain events
     */
    public List<Object> getDomainEvents() {
        List<Object> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    /**
     * Clear domain events
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }


    // Getters
    public RobotId getRobotId() { return robotId; }
    public String getModel() { return model; }
    public RobotStatus getStatus() { return status; }
    public RobotPosition getPosition() { return position; }
    public BatteryLevel getBatteryLevel() { return batteryLevel; }
    public String getCurrentTaskId() { return currentTaskId; }
    public Set<RobotCapability> getCapabilities() { return capabilities; }
    public Map<String, Object> getHealthMetrics() { return healthMetrics; }
    public Instant getLastHeartbeat() { return lastHeartbeat; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters
    public void setRobotId(RobotId robotId) { this.robotId = robotId; }
    public void setModel(String model) { this.model = model; }
    public void setStatus(RobotStatus status) { this.status = status; }
    public void setPosition(RobotPosition position) { this.position = position; }
    public void setBatteryLevel(BatteryLevel batteryLevel) { this.batteryLevel = batteryLevel; }
    public void setCurrentTaskId(String currentTaskId) { this.currentTaskId = currentTaskId; }
    public void setCapabilities(Set<RobotCapability> capabilities) { this.capabilities = capabilities; }
    public void setHealthMetrics(Map<String, Object> healthMetrics) { this.healthMetrics = healthMetrics; }
    public void setLastHeartbeat(Instant lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
