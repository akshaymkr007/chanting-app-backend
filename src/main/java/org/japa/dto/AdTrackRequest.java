package org.japa.dto;

import jakarta.validation.constraints.NotBlank;

public class AdTrackRequest {
    @NotBlank
    private String campaignId;

    @NotBlank
    private String event; // 'impression' or 'click'

    public AdTrackRequest() {}

    public String getCampaignId() { return campaignId; }
    public void setCampaignId(String campaignId) { this.campaignId = campaignId; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }
}
