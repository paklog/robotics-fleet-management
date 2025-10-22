package com.paklog.robotics.fleet.management.domain.aggregate;

import com.paklog.robotics.fleet.management.domain.event.FleetRebalancedEvent;
import com.paklog.robotics.fleet.management.domain.valueobject.*;
import lombok.Getter;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Fleet Aggregate
 * Manages a fleet of robots and workload distribution
 */
@Getter
public class Fleet {

    private String fleetId;
    private Map<String, Robot> robots;
    private int activeTaskCount;
    private int idleRobotCount;
    private double utilizationRate;
    private Instant lastRebalanceAt;
    private Instant createdAt;
    private Instant updatedAt;

    private static final double TARGET_UTILIZATION = 0.85;
    private static final int REBALANCE_THRESHOLD = 20; // 20% imbalance

    private final List<Object> domainEvents = new ArrayList<>();

    private Fleet() {
        this.robots = new HashMap<>();
    }

    /**
     * Factory method to create a fleet
     */
    public static Fleet create(String fleetId) {
        Fleet fleet = new Fleet();
        fleet.fleetId = fleetId;
        fleet.createdAt = Instant.now();
        fleet.updatedAt = Instant.now();
        return fleet;
    }

    /**
     * Add robot to fleet
     */
    public void addRobot(Robot robot) {
        robots.put(robot.getRobotId().getValue(), robot);
        recalculateMetrics();
    }

    /**
     * Remove robot from fleet
     */
    public void removeRobot(String robotId) {
        robots.remove(robotId);
        recalculateMetrics();
    }

    /**
     * Get available robots with required capability
     */
    public List<Robot> getAvailableRobots(RobotCapability capability) {
        return robots.values().stream()
            .filter(Robot::isAvailable)
            .filter(robot -> robot.hasRequiredCapability(capability))
            .collect(Collectors.toList());
    }

    /**
     * Find nearest available robot to target position
     */
    public Optional<Robot> findNearestAvailableRobot(RobotPosition target, RobotCapability capability) {
        return getAvailableRobots(capability).stream()
            .min(Comparator.comparingDouble(robot -> robot.distanceTo(target)));
    }

    /**
     * Calculate utilization rate
     */
    public void recalculateMetrics() {
        int totalRobots = robots.size();
        if (totalRobots == 0) {
            this.utilizationRate = 0.0;
            this.idleRobotCount = 0;
            this.activeTaskCount = 0;
            return;
        }

        long idleCount = robots.values().stream()
            .filter(robot -> robot.getStatus() == RobotStatus.IDLE)
            .count();

        long executingCount = robots.values().stream()
            .filter(robot -> robot.getStatus() == RobotStatus.EXECUTING)
            .count();

        this.idleRobotCount = (int) idleCount;
        this.activeTaskCount = (int) executingCount;
        this.utilizationRate = (double) executingCount / totalRobots;
        this.updatedAt = Instant.now();
    }

    /**
     * Check if fleet needs rebalancing
     */
    public boolean needsRebalancing() {
        if (robots.size() < 2) {
            return false;
        }

        // Calculate workload variance
        long maxWorkload = robots.values().stream()
            .filter(robot -> robot.getCurrentTaskId() != null)
            .count();

        long minWorkload = robots.values().stream()
            .filter(robot -> robot.getCurrentTaskId() == null && robot.isAvailable())
            .count();

        if (maxWorkload == 0) {
            return false;
        }

        double imbalance = ((double) (maxWorkload - minWorkload) / maxWorkload) * 100;
        return imbalance > REBALANCE_THRESHOLD;
    }

    /**
     * Rebalance workload across fleet
     */
    public void rebalanceWorkload() {
        recalculateMetrics();

        if (!needsRebalancing()) {
            return;
        }

        this.lastRebalanceAt = Instant.now();
        this.updatedAt = Instant.now();

        addDomainEvent(new FleetRebalancedEvent(
            fleetId,
            robots.size(),
            utilizationRate,
            Instant.now()
        ));
    }

    /**
     * Get fleet health status
     */
    public Map<String, Object> getHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        health.put("totalRobots", robots.size());
        health.put("idleRobots", idleRobotCount);
        health.put("activeTasks", activeTaskCount);
        health.put("utilizationRate", utilizationRate);
        health.put("targetUtilization", TARGET_UTILIZATION);

        long healthyRobots = robots.values().stream()
            .filter(Robot::isHealthy)
            .count();
        health.put("healthyRobots", healthyRobots);

        return health;
    }

    /**
     * Get robots by status
     */
    public List<Robot> getRobotsByStatus(RobotStatus status) {
        return robots.values().stream()
            .filter(robot -> robot.getStatus() == status)
            .collect(Collectors.toList());
    }

    /**
     * Check if utilization is within target
     */
    public boolean isUtilizationHealthy() {
        return Math.abs(utilizationRate - TARGET_UTILIZATION) < 0.15; // Within 15%
    }

    private void addDomainEvent(Object event) {
        this.domainEvents.add(event);
    }

    public List<Object> getDomainEvents() {
        List<Object> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }
}
