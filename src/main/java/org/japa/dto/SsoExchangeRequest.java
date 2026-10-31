package org.japa.dto;

import jakarta.validation.constraints.NotBlank;

public class SsoExchangeRequest {
    @NotBlank
    private String provider; // google, yahoo, linkedin

    private String ssoToken;
    private String email;
    private String displayName;
    private String spiritualName;

    public SsoExchangeRequest() {}

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getSsoToken() { return ssoToken; }
    public void setSsoToken(String ssoToken) { this.ssoToken = ssoToken; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getSpiritualName() { return spiritualName; }
    public void setSpiritualName(String spiritualName) { this.spiritualName = spiritualName; }
}
