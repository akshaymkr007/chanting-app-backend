package org.japa.dto;

import java.time.LocalDate;

public class ChantRecordDto {
    private String id;
    private String userId;
    private String date; // YYYY-MM-DD
    private int count;
    private int malas;
    private String mantraId;
    private String mantraName;
    private int durationSeconds;
    private String encryptedNote;
    private Long timestamp;

    public ChantRecordDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public int getMalas() { return malas; }
    public void setMalas(int malas) { this.malas = malas; }

    public String getMantraId() { return mantraId; }
    public void setMantraId(String mantraId) { this.mantraId = mantraId; }

    public String getMantraName() { return mantraName; }
    public void setMantraName(String mantraName) { this.mantraName = mantraName; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public String getEncryptedNote() { return encryptedNote; }
    public void setEncryptedNote(String encryptedNote) { this.encryptedNote = encryptedNote; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
