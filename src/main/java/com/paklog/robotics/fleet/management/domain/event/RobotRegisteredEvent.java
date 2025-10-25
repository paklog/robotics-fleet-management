package com.paklog.robotics.fleet.management.domain.event;

import com.paklog.robotics.fleet.management.domain.valueobject.RobotCapability;
import com.paklog.robotics.fleet.management.domain.valueobject.RobotPosition;

import java.time.Instant;
import java.util.Set;

public class RobotRegisteredEvent {
    private String robotId;
    private String model;
    private RobotPosition initialPosition;
    private Set<RobotCapability> capabilities;
    private Instant occurredAt;

    public RobotRegisteredEvent(String robotId, String model, RobotPosition initialPosition, Set<RobotCapability> capabilities, Instant occurredAt) {
        this.robotId = robotId;
        this.model = model;
        this.initialPosition = initialPosition;
        this.capabilities = capabilities;
        this.occurredAt = occurredAt;
    }

    public String getRobotId() { return robotId; }
    public String getModel() { return model; }
    public RobotPosition getInitialPosition() { return initialPosition; }
    public Set<RobotCapability> getCapabilities() { return capabilities; }
    public Instant getOccurredAt() { return occurredAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String robotId;
        private String model;
        private RobotPosition initialPosition;
        private Set<RobotCapability> capabilities;
        private Instant occurredAt;

        public Builder robotId(String robotId) { this.robotId = robotId; return this; }
        public Builder model(String model) { this.model = model; return this; }
        public Builder initialPosition(RobotPosition initialPosition) { this.initialPosition = initialPosition; return this; }
        public Builder capabilities(Set<RobotCapability> capabilities) { this.capabilities = capabilities; return this; }
        public Builder occurredAt(Instant occurredAt) { this.occurredAt = occurredAt; return this; }

        public RobotRegisteredEvent build() {
            return new RobotRegisteredEvent(robotId, model, initialPosition, capabilities, occurredAt);
        }
    }
}
