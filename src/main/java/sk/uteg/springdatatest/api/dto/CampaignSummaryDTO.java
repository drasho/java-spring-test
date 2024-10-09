package sk.uteg.springdatatest.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class CampaignSummaryDTO {
    private long totalFeedbacks;
    private List<QuestionSummaryDTO> questionSummaries;
}
