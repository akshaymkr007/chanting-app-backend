package org.japa.dto;

public class AuthResponse {
    private String token;
    private UserDto user;
    private String message;

    public AuthResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public String getToken() { return token; }
    public UserDto getUser() { return user; }
    public String getMessage() { return message; }

    public static class UserDto {
        private String id;
        private String email;
        private String displayName;
        private String spiritualName;
        private String authProvider;

        public UserDto(String id, String email, String displayName, String spiritualName, String authProvider) {
            this.id = id;
            this.email = email;
            this.displayName = displayName;
            this.spiritualName = spiritualName;
            this.authProvider = authProvider;
        }

        public String getId() { return id; }
        public String getEmail() { return email; }
        public String getDisplayName() { return displayName; }
        public String getSpiritualName() { return spiritualName; }
        public String getAuthProvider() { return authProvider; }
    }
}
