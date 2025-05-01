package uax.madm.devops.campaigns_demo.application.services;

import java.util.List;
import java.util.Optional;

import uax.madm.devops.campaigns_demo.domain.model.Campaign;

public interface CampaignService {

    List<Campaign> findCampaigns();

    Optional<Campaign> findById(Long id);

    Campaign save(Campaign campaign);

    Optional<Campaign> updateCampaign(Long id, Campaign campaign);
}
