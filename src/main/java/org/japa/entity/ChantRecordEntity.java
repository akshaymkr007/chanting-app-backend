package org.japa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Instant;

@Entity
@Table(name = "chant_records", uniqueConstraints = {
    @UniqueConstraint(name = "uq_user_date_mantra", columnNames = {"user_id", "record_date", "mantra_id"})
})
public class ChantRecordEntity {

    @Id
    @Column(name = "id", length = 64, nullable = false)
    private String id;

    @Column(name = "user_id", length = 64, nullable = false)
    private String userId;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "mantra_id", length = 64, nullable = false)
    private String mantraId;

    @Column(name = "mantra_name", length = 128, nullable = false)
    private String mantraName;

    @Column(name = "count", nullable = false)
    private int count = 0;

    @Column(name = "malas", nullable = false)
    private int malas = 0;

    @Column(name = "duration_seconds", nullable = false)
    private int durationSeconds = 0;

    @Column(name = "encrypted_note", columnDefinition = "TEXT")
    private String encryptedNote;

    @Column(name = "timestamp_ms")
    private Long timestampMs;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    public ChantRecordEntity() {}

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public String getMantraId() { return mantraId; }
    public void setMantraId(String mantraId) { this.mantraId = mantraId; }

    public String getMantraName() { return mantraName; }
    public void setMantraName(String mantraName) { this.mantraName = mantraName; }

    public int getCount() { return count; }
    public void setCount(int count) {
        this.count = count;
        this.malas = count / 108;
    }

    public int getMalas() { return malas; }
    public void setMalas(int malas) { this.malas = malas; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public String getEncryptedNote() { return encryptedNote; }
    public void setEncryptedNote(String encryptedNote) { this.encryptedNote = encryptedNote; }

    public Long getTimestampMs() { return timestampMs; }
    public void setTimestampMs(Long timestampMs) { this.timestampMs = timestampMs; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
