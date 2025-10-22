package com.paklog.robotics.fleet.management.domain.aggregate;

import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

/**
 * ChargingStation Aggregate
 * Manages charging station capacity and robot queue
 */
@Getter
public class ChargingStation {

    private String stationId;
    private RobotPosition location;
    private int capacity;
    private int availableSlots;
    private Queue<String> queuedRobots;
    private Map<String, Instant> chargingRobots; // robotId -> charging started time
    private Instant createdAt;
    private Instant updatedAt;

    private static final int DEFAULT_CHARGING_TIME_MINUTES = 30;

    private ChargingStation() {
        this.queuedRobots = new LinkedList<>();
        this.chargingRobots = new HashMap<>();
    }

    /**
     * Factory method to create a charging station
     */
    public static ChargingStation create(String stationId, RobotPosition location, int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        ChargingStation station = new ChargingStation();
        station.stationId = stationId;
        station.location = location;
        station.capacity = capacity;
        station.availableSlots = capacity;
        station.createdAt = Instant.now();
        station.updatedAt = Instant.now();
        return station;
    }

    /**
     * Add robot to charging queue
     */
    public void addToQueue(String robotId) {
        if (chargingRobots.containsKey(robotId) || queuedRobots.contains(robotId)) {
            throw new IllegalStateException("Robot is already in queue or charging");
        }

        queuedRobots.offer(robotId);
        this.updatedAt = Instant.now();
    }

    /**
     * Start charging for a robot
     */
    public void startCharging(String robotId) {
        if (availableSlots <= 0) {
            throw new IllegalStateException("No available charging slots");
        }

        if (!queuedRobots.contains(robotId) && !chargingRobots.containsKey(robotId)) {
            throw new IllegalStateException("Robot is not in queue");
        }

        queuedRobots.remove(robotId);
        chargingRobots.put(robotId, Instant.now());
        availableSlots--;
        this.updatedAt = Instant.now();
    }

    /**
     * Release robot from charging
     */
    public void releaseRobot(String robotId) {
        if (!chargingRobots.containsKey(robotId)) {
            throw new IllegalStateException("Robot is not charging at this station");
        }

        chargingRobots.remove(robotId);
        availableSlots++;
        this.updatedAt = Instant.now();

        // Start charging next robot in queue if available
        if (!queuedRobots.isEmpty() && availableSlots > 0) {
            String nextRobotId = queuedRobots.peek();
            startCharging(nextRobotId);
        }
    }

    /**
     * Check if station has available slots
     */
    public boolean isAvailable() {
        return availableSlots > 0;
    }

    /**
     * Get queue position for a robot
     */
    public int getQueuePosition(String robotId) {
        if (chargingRobots.containsKey(robotId)) {
            return 0; // Currently charging
        }

        List<String> queueList = new ArrayList<>(queuedRobots);
        int position = queueList.indexOf(robotId);
        return position >= 0 ? position + 1 : -1;
    }

    /**
     * Get current utilization rate
     */
    public double getUtilizationRate() {
        return (double) (capacity - availableSlots) / capacity;
    }

    /**
     * Get queue length
     */
    public int getQueueLength() {
        return queuedRobots.size();
    }

    /**
     * Estimate wait time for a robot in queue (in minutes)
     */
    public int estimateWaitTime(String robotId) {
        int position = getQueuePosition(robotId);
        if (position <= 0) {
            return 0;
        }

        // Estimate based on position and average charging time
        return (position - 1) * (DEFAULT_CHARGING_TIME_MINUTES / capacity);
    }

    /**
     * Get robots currently charging
     */
    public Set<String> getChargingRobots() {
        return new HashSet<>(chargingRobots.keySet());
    }

    /**
     * Check if robot is charging
     */
    public boolean isRobotCharging(String robotId) {
        return chargingRobots.containsKey(robotId);
    }

    /**
     * Get charging duration for a robot (in minutes)
     */
    public long getChargingDuration(String robotId) {
        Instant startTime = chargingRobots.get(robotId);
        if (startTime == null) {
            return 0;
        }

        long seconds = Instant.now().getEpochSecond() - startTime.getEpochSecond();
        return seconds / 60;
    }
}
