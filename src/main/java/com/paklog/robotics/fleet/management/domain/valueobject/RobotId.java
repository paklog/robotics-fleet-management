package com.paklog.robotics.fleet.management.domain.valueobject;

import java.io.Serializable;
import java.util.UUID;

/**
 * Robot ID Value Object
 */
public record RobotId(
    String value
) implements Serializable {

    public String getValue() {
        return value;
    }

    public static RobotId of(String value) {
        return new RobotId(value);
    }

    public static RobotId generate() {
        return new RobotId(UUID.randomUUID().toString());
    }
}
