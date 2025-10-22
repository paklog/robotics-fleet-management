package com.paklog.robotics.fleet.management.domain.valueobject;

/**
 * Task Type Enumeration
 */
public enum TaskType {
    MOVE,       // Move to location
    PICK,       // Pick items
    TRANSPORT,  // Transport goods
    SORT,       // Sort items
    DELIVER,    // Deliver to destination
    RETURN      // Return to base
}
