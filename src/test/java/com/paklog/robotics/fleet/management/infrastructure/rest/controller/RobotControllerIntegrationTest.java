package com.paklog.robotics.fleet.management.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paklog.robotics.fleet.management.domain.valueobject.RobotCapability;
import com.paklog.robotics.fleet.management.infrastructure.rest.dto.RegisterRobotRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class RobotControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterRobotSuccessfully() throws Exception {
        RegisterRobotRequest request = new RegisterRobotRequest(
            "ROBOT-TEST-001",
            "AMR-X1",
            10.0,
            20.0,
            0.0,
            Set.of(RobotCapability.PICKER, RobotCapability.TRANSPORTER)
        );

        mockMvc.perform(post("/api/v1/robots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.robotId").value("ROBOT-TEST-001"))
            .andExpect(jsonPath("$.model").value("AMR-X1"))
            .andExpect(jsonPath("$.status").value("IDLE"))
            .andExpect(jsonPath("$.batteryLevel").value(100));
    }

    @Test
    void shouldGetRobotById() throws Exception {
        // First register a robot
        RegisterRobotRequest request = new RegisterRobotRequest(
            "ROBOT-TEST-002",
            "AMR-X2",
            15.0,
            25.0,
            90.0,
            Set.of(RobotCapability.PICKER)
        );

        mockMvc.perform(post("/api/v1/robots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        // Then retrieve it
        mockMvc.perform(get("/api/v1/robots/ROBOT-TEST-002"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.robotId").value("ROBOT-TEST-002"))
            .andExpect(jsonPath("$.model").value("AMR-X2"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentRobot() throws Exception {
        mockMvc.perform(get("/api/v1/robots/NON-EXISTENT"))
            .andExpect(status().isNotFound());
    }
}
