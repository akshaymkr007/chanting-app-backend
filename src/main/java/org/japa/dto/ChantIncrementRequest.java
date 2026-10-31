package org.japa.dto;

import jakarta.validation.constraints.NotBlank;

public class ChantIncrementRequest {
    private String userId;
    private String date; // YYYY-MM-DD (optional, defaults to current date)

    @NotBlank
    private String mantraId;

    private String mantraName;
    private int count = 1;
    private int durationSeconds = 2;
    private String encryptedNote;

    public ChantIncrementRequest() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getMantraId() { return mantraId; }
    public void setMantraId(String mantraId) { this.mantraId = mantraId; }

    public String getMantraName() { return mantraName; }
    public void setMantraName(String mantraName) { this.mantraName = mantraName; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public String getEncryptedNote() { return encryptedNote; }
    public void setEncryptedNote(String encryptedNote) { this.encryptedNote = encryptedNote; }
}
