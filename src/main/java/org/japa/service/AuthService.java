package org.japa.service;

import org.japa.dto.AuthResponse;
import org.japa.dto.LoginRequest;
import org.japa.dto.SsoExchangeRequest;
import org.japa.entity.UserEntity;
import org.japa.repository.UserRepository;
import org.japa.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse loginOrRegister(LoginRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
        UserEntity user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
            if (user.getPasswordHash() != null && !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Invalid password credentials.");
            }
            if (request.getSpiritualName() != null && !request.getSpiritualName().isBlank()) {
                user.setSpiritualName(request.getSpiritualName());
                userRepository.save(user);
            }
        } else {
            String userId = "usr_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            String displayName = request.getEmail().split("@")[0];
            String spiritualName = (request.getSpiritualName() != null && !request.getSpiritualName().isBlank())
                    ? request.getSpiritualName()
                    : "Sadhaka " + displayName;

            user = new UserEntity(
                    userId,
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword()),
                    displayName,
                    spiritualName,
                    "self-oauth"
            );
            user = userRepository.save(user);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), "ROLE_USER");
        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getSpiritualName(),
                user.getAuthProvider()
        );

        return new AuthResponse(token, userDto, "Authenticated successfully via Self-OAuth JWT Server");
    }

    @Transactional
    public AuthResponse ssoExchange(SsoExchangeRequest request) {
        String email = (request.getEmail() != null && !request.getEmail().isBlank())
                ? request.getEmail()
                : request.getProvider() + "_seeker@spiritualjapa.org";

        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        UserEntity user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
            user.setProviderTokenId(request.getSsoToken());
            userRepository.save(user);
        } else {
            String userId = "usr_" + request.getProvider() + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            String displayName = (request.getDisplayName() != null) ? request.getDisplayName() : request.getProvider() + " Seeker";
            String spiritualName = (request.getSpiritualName() != null) ? request.getSpiritualName() : "Bhakta (" + request.getProvider() + ")";

            user = new UserEntity(
                    userId,
                    email,
                    null, // No password for SSO users
                    displayName,
                    spiritualName,
                    request.getProvider().toLowerCase()
            );
            user.setProviderTokenId(request.getSsoToken());
            user = userRepository.save(user);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), "ROLE_USER");
        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getSpiritualName(),
                user.getAuthProvider()
        );

        return new AuthResponse(token, userDto, "Authenticated successfully via " + request.getProvider().toUpperCase() + " SSO");
    }
}
