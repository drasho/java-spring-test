package sk.uteg.springdatatest.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.uteg.springdatatest.api.dto.CampaignSummaryDTO;
import sk.uteg.springdatatest.mapper.CampaignMapper;
import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.service.CampaignService;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = {CampaignController.class})
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;

    @Test
    void givenCampaignExists_whenGetSummary_thenReturnSummary() throws Exception {
        UUID uuid = UUID.randomUUID();
        Campaign campaign = new Campaign();
        campaign.setId(uuid);
        CampaignSummaryDTO campaignSummaryDTO = CampaignMapper.INSTANCE.toCampaignSummaryDTO(campaign);
        campaignSummaryDTO.setTotalFeedbacks(3);


        Mockito.when(campaignService.getModel(any())).thenReturn(Optional.of(campaign));
        Mockito.when(campaignService.getSummary(any())).thenReturn(campaignSummaryDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/summary/" + uuid)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.totalFeedbacks").value(3),
                        MockMvcResultMatchers.jsonPath("$.questionSummaries").exists()
                );
    }

    @Test
    void givenCampaignDoesntExist_whenGetSummary_thenReturnNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        Optional<Campaign> campaign = Optional.empty();

        Mockito.when(campaignService.getModel(any())).thenReturn(campaign);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/summary/" + uuid)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}