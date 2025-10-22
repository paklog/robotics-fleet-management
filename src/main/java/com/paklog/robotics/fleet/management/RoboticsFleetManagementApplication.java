package com.paklog.robotics.fleet.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Robotics Fleet Management
 *
 * AMR/AGV fleet orchestration and robot task assignment
 *
 * @author Paklog Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableKafka
@EnableMongoAuditing
public class RoboticsFleetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoboticsFleetManagementApplication.class, args);
    }
}