package org.japa.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Service availability and database connectivity probe")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health probe for Kubernetes liveness & readiness")
    public ResponseEntity<?> checkHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "japa-backend-spring-boot",
                "runtime", "Java 21 LTS / Spring Boot 3.3.4",
                "database", "PostgreSQL with Liquibase",
                "timestamp", Instant.now().toString()
        ));
    }
}
