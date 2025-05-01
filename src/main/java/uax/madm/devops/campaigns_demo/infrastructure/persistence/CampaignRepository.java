package uax.madm.devops.campaigns_demo.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import uax.madm.devops.campaigns_demo.domain.model.Campaign;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
