package sk.uteg.springdatatest.service;

import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.dto.CampaignSummaryDTO;
import sk.uteg.springdatatest.mapper.CampaignMapper;
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

    /**
     * Fetches given Campaign from database by its UUID
     *
     * @param uuid
     * @return
     */
    public Optional<Campaign> getModel(UUID uuid) {
        return campaignRepository.findById(uuid);
    }

    /**
     * Transforms given Campaign into a CampaignSummaryDTO
     *
     * @param campaign
     * @return
     */
    public CampaignSummaryDTO getSummary(Campaign campaign) {
        return CampaignMapper.INSTANCE.toCampaignSummaryDTO(campaign);
    }
}
