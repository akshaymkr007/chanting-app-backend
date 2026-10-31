package org.japa.dto;

import java.util.List;

public class AdConfigResponse {
    private boolean googleAdSenseEnabled;
    private String googleAdSensePublisherId;
    private String googleAdSenseSlotId;
    private boolean customPromotionEnabled;
    private List<CustomAdCampaignDto> customPromotions;

    public AdConfigResponse() {}

    public boolean isGoogleAdSenseEnabled() { return googleAdSenseEnabled; }
    public void setGoogleAdSenseEnabled(boolean googleAdSenseEnabled) { this.googleAdSenseEnabled = googleAdSenseEnabled; }

    public String getGoogleAdSensePublisherId() { return googleAdSensePublisherId; }
    public void setGoogleAdSensePublisherId(String googleAdSensePublisherId) { this.googleAdSensePublisherId = googleAdSensePublisherId; }

    public String getGoogleAdSenseSlotId() { return googleAdSenseSlotId; }
    public void setGoogleAdSenseSlotId(String googleAdSenseSlotId) { this.googleAdSenseSlotId = googleAdSenseSlotId; }

    public boolean isCustomPromotionEnabled() { return customPromotionEnabled; }
    public void setCustomPromotionEnabled(boolean customPromotionEnabled) { this.customPromotionEnabled = customPromotionEnabled; }

    public List<CustomAdCampaignDto> getCustomPromotions() { return customPromotions; }
    public void setCustomPromotions(List<CustomAdCampaignDto> customPromotions) { this.customPromotions = customPromotions; }

    public static class CustomAdCampaignDto {
        private String id;
        private String title;
        private String subtitle;
        private String ctaText;
        private String ctaUrl;
        private String tag;
        private boolean active;
        private long impressions;
        private long clicks;

        public CustomAdCampaignDto() {}

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
    }
}
