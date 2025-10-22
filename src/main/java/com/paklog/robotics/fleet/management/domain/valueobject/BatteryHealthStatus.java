package com.paklog.robotics.fleet.management.domain.valueobject;

/**
 * Battery Health Status Enumeration
 */
public enum BatteryHealthStatus {
    CRITICAL,   // <= 10%
    LOW,        // <= 20%
    MARGINAL,   // < 30%
    NORMAL,     // 30-79%
    FULL        // >= 80%
}
