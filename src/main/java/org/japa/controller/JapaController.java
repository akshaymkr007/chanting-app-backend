package org.japa.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.japa.dto.*;
import org.japa.service.JapaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/japa")
@Tag(name = "Japa Counter & Analytics", description = "Endpoints for batch offline sync, chant increments, and historical stats")
public class JapaController {

    private final JapaService japaService;

    public JapaController(JapaService japaService) {
        this.japaService = japaService;
    }

    @PostMapping("/sync")
    @Operation(summary = "Batch synchronize queued offline records from Web, Android, or iOS")
    public ResponseEntity<ChantSyncResponse> syncRecords(
            Authentication authentication,
            @Valid @RequestBody ChantSyncRequest request) {
        String authUserId = (authentication != null && authentication.getPrincipal() != null)
                ? authentication.getPrincipal().toString()
                : request.getUserId();

        int syncedCount = japaService.batchSync(authUserId, request.getRecords());
        return ResponseEntity.ok(new ChantSyncResponse("SUCCESS", syncedCount, System.currentTimeMillis()));
    }

    @PostMapping("/increment")
    @Operation(summary = "Real-time chant increment (+1 or batch)")
    public ResponseEntity<ChantRecordDto> incrementChant(
            Authentication authentication,
            @Valid @RequestBody ChantIncrementRequest request) {
        if (authentication != null && authentication.getPrincipal() != null) {
            request.setUserId(authentication.getPrincipal().toString());
        }
        ChantRecordDto updated = japaService.increment(request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/history")
    @Operation(summary = "Get historical raw records for daily, weekly, or monthly charts")
    public ResponseEntity<List<ChantRecordDto>> getHistory(
            Authentication authentication,
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "30") int days) {
        String targetUserId = (authentication != null && authentication.getPrincipal() != null)
                ? authentication.getPrincipal().toString()
                : userId;

        return ResponseEntity.ok(japaService.getHistory(targetUserId, days));
    }

    @GetMapping("/analytics/daily")
    @Operation(summary = "Get aggregated daily summaries for time-series charts (7, 14, or 30 days)")
    public ResponseEntity<List<DaySummaryDto>> getDailySummaries(
            Authentication authentication,
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "14") int days) {
        String targetUserId = (authentication != null && authentication.getPrincipal() != null)
                ? authentication.getPrincipal().toString()
                : userId;

        return ResponseEntity.ok(japaService.getDailySummaries(targetUserId, days));
    }

    @GetMapping("/analytics/summary")
    @Operation(summary = "Get streaks, lifetime chant count, and total malas")
    public ResponseEntity<Map<String, Object>> getStreakAndTotals(
            Authentication authentication,
            @RequestParam(required = false) String userId) {
        String targetUserId = (authentication != null && authentication.getPrincipal() != null)
                ? authentication.getPrincipal().toString()
                : userId;

        return ResponseEntity.ok(japaService.calculateStreakAndTotals(targetUserId));
    }
}
