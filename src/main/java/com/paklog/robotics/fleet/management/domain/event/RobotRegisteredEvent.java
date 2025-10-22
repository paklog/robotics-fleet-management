package com.paklog.robotics.fleet.management.domain.event;

import com.paklog.robotics.fleet.management.domain.valueobject.RobotCapability;
import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
public class RobotRegisteredEvent {
    private String robotId;
    private String model;
    private RobotPosition initialPosition;
    private Set<RobotCapability> capabilities;
    private Instant occurredAt;
}
