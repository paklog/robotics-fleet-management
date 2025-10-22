package com.paklog.robotics.fleet.management.domain.valueobject;

/**
 * Traffic Zone Type Enumeration
 */
public enum TrafficZoneType {
    AISLE,              // Narrow passage between storage
    INTERSECTION,       // Where paths cross
    CHARGING_AREA,      // Near charging stations
    PICKING_ZONE,       // Active picking operations
    STAGING_AREA,       // Temporary holding area
    HIGH_TRAFFIC,       // Busy area
    RESTRICTED          // Limited access
}
