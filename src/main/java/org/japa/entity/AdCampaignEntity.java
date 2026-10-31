package org.japa.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ad_campaigns")
public class AdCampaignEntity {

    @Id
    @Column(name = "id", length = 64, nullable = false)
    private String id;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "subtitle", columnDefinition = "TEXT")
    private String subtitle;

    @Column(name = "cta_text", length = 64, nullable = false)
    private String ctaText;

    @Column(name = "cta_url", length = 512, nullable = false)
    private String ctaUrl;

    @Column(name = "tag", length = 64)
    private String tag;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "impressions")
    private long impressions = 0;

    @Column(name = "clicks")
    private long clicks = 0;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    public AdCampaignEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getCtaText() { return ctaText; }
    public void setCtaText(String ctaText) { this.ctaText = ctaText; }

    public String getCtaUrl() { return ctaUrl; }
    public void setCtaUrl(String ctaUrl) { this.ctaUrl = ctaUrl; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getImpressions() { return impressions; }
    public void setImpressions(long impressions) { this.impressions = impressions; }

    public long getClicks() { return clicks; }
    public void setClicks(long clicks) { this.clicks = clicks; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
