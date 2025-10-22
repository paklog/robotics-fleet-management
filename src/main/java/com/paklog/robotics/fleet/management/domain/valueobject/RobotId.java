package com.paklog.robotics.fleet.management.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Robot ID Value Object
 */
@Getter
@EqualsAndHashCode
public class RobotId implements Serializable {

    private final String value;

    private RobotId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Robot ID cannot be null or empty");
        }
        this.value = value;
    }

    public static RobotId of(String value) {
        return new RobotId(value);
    }

    public static RobotId generate() {
        return new RobotId("ROBOT-" + UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
