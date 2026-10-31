package org.japa.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ad_global_config")
public class AdGlobalConfigEntity {

    @Id
    @Column(name = "id", length = 32, nullable = false)
    private String id = "default";

    @Column(name = "google_adsense_enabled")
    private boolean googleAdSenseEnabled = false;

    @Column(name = "google_adsense_publisher_id", length = 128)
    private String googleAdSensePublisherId = "ca-pub-9876543210123456";

    @Column(name = "google_adsense_slot_id", length = 64)
    private String googleAdSenseSlotId = "1234567890";

    @Column(name = "custom_promotion_enabled")
    private boolean customPromotionEnabled = true;

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    public AdGlobalConfigEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public boolean isGoogleAdSenseEnabled() { return googleAdSenseEnabled; }
    public void setGoogleAdSenseEnabled(boolean googleAdSenseEnabled) { this.googleAdSenseEnabled = googleAdSenseEnabled; }

    public String getGoogleAdSensePublisherId() { return googleAdSensePublisherId; }
    public void setGoogleAdSensePublisherId(String googleAdSensePublisherId) { this.googleAdSensePublisherId = googleAdSensePublisherId; }

    public String getGoogleAdSenseSlotId() { return googleAdSenseSlotId; }
    public void setGoogleAdSenseSlotId(String googleAdSenseSlotId) { this.googleAdSenseSlotId = googleAdSenseSlotId; }

    public boolean isCustomPromotionEnabled() { return customPromotionEnabled; }
    public void setCustomPromotionEnabled(boolean customPromotionEnabled) { this.customPromotionEnabled = customPromotionEnabled; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
