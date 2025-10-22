package com.paklog.robotics.fleet.management.domain.valueobject;

/**
 * Robot Capability Enumeration
 * Defines what operations a robot can perform
 */
public enum RobotCapability {
    PICKER,         // Can pick items from shelves
    TRANSPORTER,    // Can transport pallets/containers
    SORTER,         // Can sort items
    LIFTER,         // Can lift heavy loads
    SCANNER,        // Has barcode/RFID scanning
    COLLABORATIVE   // Can work alongside humans
}
