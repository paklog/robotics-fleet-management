package com.paklog.robotics.fleet.management.domain.valueobject;

/**
 * Robot Status Enumeration
 */
public enum RobotStatus {
    IDLE,           // Available for tasks
    EXECUTING,      // Currently executing a task
    CHARGING,       // Charging battery
    MAINTENANCE,    // Under maintenance
    ERROR,          // Error state
    OFFLINE         // Not connected
}
