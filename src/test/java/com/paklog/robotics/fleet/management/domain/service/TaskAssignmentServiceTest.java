package com.paklog.robotics.fleet.management.domain.service;

import com.paklog.robotics.fleet.management.domain.aggregate.Robot;
import com.paklog.robotics.fleet.management.domain.aggregate.RobotTask;
import com.paklog.robotics.fleet.management.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskAssignmentServiceTest {

    private TaskAssignmentService taskAssignmentService;

    @BeforeEach
    void setUp() {
        taskAssignmentService = new TaskAssignmentService();
    }

    @Test
    void shouldFindOptimalRobot() {
        Robot robot1 = Robot.register(
            RobotId.of("ROBOT-001"),
            "AMR-X1",
            RobotPosition.of(10.0, 10.0, 0.0),
            Set.of(RobotCapability.PICKER)
        );

        Robot robot2 = Robot.register(
            RobotId.of("ROBOT-002"),
            "AMR-X2",
            RobotPosition.of(50.0, 50.0, 0.0),
            Set.of(RobotCapability.PICKER)
        );

        RobotTask task = RobotTask.create(
            "TASK-001",
            TaskType.PICK,
            TaskPriority.HIGH,
            RobotPosition.of(15.0, 15.0, 0.0),
            RobotPosition.of(20.0, 20.0, 0.0),
            RobotCapability.PICKER,
            null
        );

        Optional<Robot> optimal = taskAssignmentService.findOptimalRobot(
            task,
            List.of(robot1, robot2)
        );

        assertTrue(optimal.isPresent());
        assertEquals("ROBOT-001", optimal.get().getRobotId().getValue()); // Closer robot
    }

    @Test
    void shouldNotFindRobotWithoutCapability() {
        Robot robot = Robot.register(
            RobotId.of("ROBOT-001"),
            "AMR-X1",
            RobotPosition.of(10.0, 10.0, 0.0),
            Set.of(RobotCapability.TRANSPORTER)
        );

        RobotTask task = RobotTask.create(
            "TASK-001",
            TaskType.PICK,
            TaskPriority.HIGH,
            RobotPosition.of(15.0, 15.0, 0.0),
            RobotPosition.of(20.0, 20.0, 0.0),
            RobotCapability.PICKER,
            null
        );

        Optional<Robot> optimal = taskAssignmentService.findOptimalRobot(
            task,
            List.of(robot)
        );

        assertFalse(optimal.isPresent());
    }

    @Test
    void shouldValidateRobotCanAcceptTask() {
        Robot robot = Robot.register(
            RobotId.of("ROBOT-001"),
            "AMR-X1",
            RobotPosition.of(10.0, 10.0, 0.0),
            Set.of(RobotCapability.PICKER)
        );

        RobotTask task = RobotTask.create(
            "TASK-001",
            TaskType.PICK,
            TaskPriority.HIGH,
            RobotPosition.of(15.0, 15.0, 0.0),
            RobotPosition.of(20.0, 20.0, 0.0),
            RobotCapability.PICKER,
            null
        );

        assertTrue(taskAssignmentService.canRobotAcceptTask(robot, task));
    }
}
