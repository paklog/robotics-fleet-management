package com.paklog.robotics.fleet.management.domain.valueobject;

/**
 * Task Status Enumeration
 */
public enum TaskStatus {
    PENDING,    // Task created, not assigned
    ASSIGNED,   // Assigned to robot
    IN_PROGRESS,// Robot executing task
    COMPLETED,  // Task completed successfully
    FAILED,     // Task failed
    CANCELLED   // Task cancelled
}
