package sk.uteg.springdatatest.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sk.uteg.springdatatest.api.dto.CampaignSummaryDTO;
import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.service.CampaignService;

import java.util.Optional;
import java.util.UUID;

@RestController("campaign")
public class CampaignController {

    Logger logger = LoggerFactory.getLogger(CampaignController.class);

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    /**
     * Returns summary for given Campaign
     *
     * @param uuid
     * @return
     */
    @GetMapping("/summary/{uuid}")
    public ResponseEntity<CampaignSummaryDTO> getSummary(@PathVariable UUID uuid) {
        Optional<Campaign> optionalCampaign = campaignService.getModel(uuid);

        if(optionalCampaign.isPresent()) {
            return ResponseEntity.ok(campaignService.getSummary(optionalCampaign.get()));
        } else {
            logger.error("Campaign for summary not found: {}", uuid);
            return ResponseEntity.notFound().build();
        }
    }
}
