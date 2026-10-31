package org.japa.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.japa.dto.AdConfigResponse;
import org.japa.dto.AdTrackRequest;
import org.japa.service.AdsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ads")
@Tag(name = "Ads & Promotions", description = "Google AdSense configuration and devotional campaign telemetry")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping("/config")
    @Operation(summary = "Retrieve active ad configuration and custom promotional announcements")
    public ResponseEntity<AdConfigResponse> getAdConfig() {
        return ResponseEntity.ok(adsService.getAdConfig());
    }

    @PostMapping("/config")
    @Operation(summary = "Admin update of Google AdSense and promotional campaigns")
    public ResponseEntity<AdConfigResponse> updateAdConfig(@RequestBody AdConfigResponse request) {
        return ResponseEntity.ok(adsService.updateAdConfig(request));
    }

    @PostMapping("/track")
    @Operation(summary = "Track impression or click event for a promotion")
    public ResponseEntity<?> trackEvent(@Valid @RequestBody AdTrackRequest request) {
        adsService.trackEvent(request);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
