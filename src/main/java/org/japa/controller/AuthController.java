package org.japa.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.japa.dto.AuthResponse;
import org.japa.dto.LoginRequest;
import org.japa.dto.SsoExchangeRequest;
import org.japa.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Self-OAuth JWT login/registration and 3rd party SSO exchange")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Sign in or register with email and password via Self-OAuth server")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.loginOrRegister(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sso-token-exchange")
    @Operation(summary = "Exchange 3rd party SSO identity token (Google, Yahoo, LinkedIn) for a JWT session token")
    public ResponseEntity<AuthResponse> ssoExchange(@Valid @RequestBody SsoExchangeRequest request) {
        AuthResponse response = authService.ssoExchange(request);
        return ResponseEntity.ok(response);
    }
}
