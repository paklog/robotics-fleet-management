package com.paklog.robotics.fleet.management.domain.aggregate;

import com.paklog.robotics.fleet.management.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    private Robot robot;
    private RobotTask task;

    @BeforeEach
    void setUp() {
        RobotId robotId = RobotId.of("ROBOT-001");
        RobotPosition initialPosition = RobotPosition.of(10.0, 20.0, 0.0);
        Set<RobotCapability> capabilities = Set.of(RobotCapability.PICKER, RobotCapability.TRANSPORTER);

        robot = Robot.register(robotId, "AMR-X1", initialPosition, capabilities);

        RobotPosition origin = RobotPosition.of(15.0, 25.0, 0.0);
        RobotPosition destination = RobotPosition.of(30.0, 40.0, 0.0);

        task = RobotTask.create(
            "TASK-001",
            TaskType.PICK,
            TaskPriority.HIGH,
            origin,
            destination,
            RobotCapability.PICKER,
            null
        );
    }

    @Test
    void shouldRegisterRobotSuccessfully() {
        assertNotNull(robot);
        assertEquals(RobotStatus.IDLE, robot.getStatus());
        assertEquals(100, robot.getBatteryLevel().getPercentage());
        assertTrue(robot.isAvailable());
    }

    @Test
    void shouldAssignTaskToRobot() {
        robot.assignTask(task);

        assertEquals(RobotStatus.EXECUTING, robot.getStatus());
        assertEquals("TASK-001", robot.getCurrentTaskId());
        assertFalse(robot.isAvailable());
    }

    @Test
    void shouldNotAssignTaskWhenBatteryLow() {
        robot.updateBatteryLevel(25);

        assertThrows(IllegalStateException.class, () -> robot.assignTask(task));
    }

    @Test
    void shouldCompleteTaskSuccessfully() {
        robot.assignTask(task);
        robot.startTask();
        robot.completeTask();

        assertEquals(RobotStatus.IDLE, robot.getStatus());
        assertNull(robot.getCurrentTaskId());
        assertTrue(robot.isAvailable());
    }

    @Test
    void shouldSendToChargingWhenBatteryLow() {
        robot.updateBatteryLevel(15);
        robot.sendToCharging();

        assertEquals(RobotStatus.CHARGING, robot.getStatus());
    }

    @Test
    void shouldCompleteCharging() {
        robot.updateBatteryLevel(15);
        robot.sendToCharging();
        robot.completeCharging();

        assertEquals(RobotStatus.IDLE, robot.getStatus());
        assertEquals(100, robot.getBatteryLevel().getPercentage());
    }

    @Test
    void shouldCheckBatteryLevel() {
        robot.updateBatteryLevel(15);

        assertTrue(robot.getBatteryLevel().needsCharging());
        assertFalse(robot.getBatteryLevel().isSufficientForTask());
    }

    @Test
    void shouldUpdatePosition() {
        RobotPosition newPosition = RobotPosition.of(50.0, 60.0, 90.0);
        robot.updatePosition(newPosition);

        assertEquals(50.0, robot.getPosition().getX());
        assertEquals(60.0, robot.getPosition().getY());
        assertEquals(90.0, robot.getPosition().getHeading());
    }

    @Test
    void shouldCalculateDistanceToTarget() {
        RobotPosition target = RobotPosition.of(20.0, 30.0, 0.0);
        double distance = robot.distanceTo(target);

        assertTrue(distance > 0);
    }
}
