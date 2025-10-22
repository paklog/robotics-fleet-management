package com.paklog.robotics.fleet.management.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * Battery Level Value Object
 * Represents robot battery percentage with health status
 */
@Getter
@EqualsAndHashCode
public class BatteryLevel implements Serializable {

    private final int percentage;
    private final BatteryHealthStatus healthStatus;

    public static final int CRITICAL_THRESHOLD = 10;
    public static final int LOW_THRESHOLD = 20;
    public static final int CHARGING_BUFFER = 5;
    public static final int TASK_ASSIGNMENT_THRESHOLD = 30;

    private BatteryLevel(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Battery percentage must be between 0 and 100");
        }
        this.percentage = percentage;
        this.healthStatus = determineHealthStatus(percentage);
    }

    public static BatteryLevel of(int percentage) {
        return new BatteryLevel(percentage);
    }

    private BatteryHealthStatus determineHealthStatus(int percentage) {
        if (percentage <= CRITICAL_THRESHOLD) {
            return BatteryHealthStatus.CRITICAL;
        } else if (percentage <= LOW_THRESHOLD) {
            return BatteryHealthStatus.LOW;
        } else if (percentage < TASK_ASSIGNMENT_THRESHOLD) {
            return BatteryHealthStatus.MARGINAL;
        } else if (percentage < 80) {
            return BatteryHealthStatus.NORMAL;
        } else {
            return BatteryHealthStatus.FULL;
        }
    }

    public boolean isCritical() {
        return healthStatus == BatteryHealthStatus.CRITICAL;
    }

    public boolean isLow() {
        return healthStatus == BatteryHealthStatus.LOW;
    }

    public boolean isSufficientForTask() {
        return percentage > TASK_ASSIGNMENT_THRESHOLD;
    }

    public boolean needsCharging() {
        return percentage <= LOW_THRESHOLD;
    }

    public boolean needsEmergencyCharging() {
        return percentage <= CRITICAL_THRESHOLD;
    }

    public boolean hasSufficientBufferForTravel() {
        return percentage > CHARGING_BUFFER;
    }

    @Override
    public String toString() {
        return String.format("Battery(%d%% - %s)", percentage, healthStatus);
    }
}
