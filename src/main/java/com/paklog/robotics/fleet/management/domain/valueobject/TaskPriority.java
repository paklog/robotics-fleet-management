package com.paklog.robotics.fleet.management.domain.valueobject;

/**
 * Task Priority Enumeration
 */
public enum TaskPriority {
    LOW(1),
    NORMAL(2),
    HIGH(3),
    URGENT(4);

    private final int level;

    TaskPriority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isHigherThan(TaskPriority other) {
        return this.level > other.level;
    }
}
