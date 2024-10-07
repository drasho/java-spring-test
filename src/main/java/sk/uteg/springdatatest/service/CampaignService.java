package sk.uteg.springdatatest.service;

import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.model.CampaignSummary;
import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.repository.CampaignRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Optional<Campaign> getModel(UUID uuid) {
        return campaignRepository.findById(uuid);
    }

    public CampaignSummary getSummary(Campaign campaign) {
        return new CampaignSummary(campaign);
    }
}
