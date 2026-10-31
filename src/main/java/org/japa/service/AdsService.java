package org.japa.service;

import org.japa.dto.AdConfigResponse;
import org.japa.dto.AdTrackRequest;
import org.japa.entity.AdCampaignEntity;
import org.japa.entity.AdGlobalConfigEntity;
import org.japa.repository.AdCampaignRepository;
import org.japa.repository.AdGlobalConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdsService {

    private final AdGlobalConfigRepository globalConfigRepository;
    private final AdCampaignRepository campaignRepository;

    public AdsService(AdGlobalConfigRepository globalConfigRepository, AdCampaignRepository campaignRepository) {
        this.globalConfigRepository = globalConfigRepository;
        this.campaignRepository = campaignRepository;
    }

    @Transactional(readOnly = true)
    public AdConfigResponse getAdConfig() {
        AdGlobalConfigEntity config = globalConfigRepository.findById("default")
                .orElseGet(() -> {
                    AdGlobalConfigEntity def = new AdGlobalConfigEntity();
                    return globalConfigRepository.save(def);
                });

        List<AdCampaignEntity> campaigns = campaignRepository.findAll();

        AdConfigResponse response = new AdConfigResponse();
        response.setGoogleAdSenseEnabled(config.isGoogleAdSenseEnabled());
        response.setGoogleAdSensePublisherId(config.getGoogleAdSensePublisherId());
        response.setGoogleAdSenseSlotId(config.getGoogleAdSenseSlotId());
        response.setCustomPromotionEnabled(config.isCustomPromotionEnabled());

        List<AdConfigResponse.CustomAdCampaignDto> dtos = campaigns.stream().map(c -> {
            AdConfigResponse.CustomAdCampaignDto dto = new AdConfigResponse.CustomAdCampaignDto();
            dto.setId(c.getId());
            dto.setTitle(c.getTitle());
            dto.setSubtitle(c.getSubtitle());
            dto.setCtaText(c.getCtaText());
            dto.setCtaUrl(c.getCtaUrl());
            dto.setTag(c.getTag());
            dto.setActive(c.isActive());
            dto.setImpressions(c.getImpressions());
            dto.setClicks(c.getClicks());
            return dto;
        }).collect(Collectors.toList());

        response.setCustomPromotions(dtos);
        return response;
    }

    @Transactional
    public AdConfigResponse updateAdConfig(AdConfigResponse request) {
        AdGlobalConfigEntity config = globalConfigRepository.findById("default")
                .orElseGet(AdGlobalConfigEntity::new);

        config.setGoogleAdSenseEnabled(request.isGoogleAdSenseEnabled());
        if (request.getGoogleAdSensePublisherId() != null) {
            config.setGoogleAdSensePublisherId(request.getGoogleAdSensePublisherId());
        }
        if (request.getGoogleAdSenseSlotId() != null) {
            config.setGoogleAdSenseSlotId(request.getGoogleAdSenseSlotId());
        }
        config.setCustomPromotionEnabled(request.isCustomPromotionEnabled());
        globalConfigRepository.save(config);

        return getAdConfig();
    }

    @Transactional
    public void trackEvent(AdTrackRequest request) {
        Optional<AdCampaignEntity> campaignOpt = campaignRepository.findById(request.getCampaignId());
        if (campaignOpt.isPresent()) {
            AdCampaignEntity campaign = campaignOpt.get();
            if ("click".equalsIgnoreCase(request.getEvent())) {
                campaign.setClicks(campaign.getClicks() + 1);
            } else if ("impression".equalsIgnoreCase(request.getEvent())) {
                campaign.setImpressions(campaign.getImpressions() + 1);
            }
            campaignRepository.save(campaign);
        }
    }
}
