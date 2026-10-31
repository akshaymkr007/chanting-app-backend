package org.japa.repository;

import org.japa.entity.AdCampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdCampaignRepository extends JpaRepository<AdCampaignEntity, String> {
    List<AdCampaignEntity> findByActiveTrue();
}
