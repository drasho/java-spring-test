package sk.uteg.springdatatest.api.model;

import lombok.Data;
import sk.uteg.springdatatest.db.model.Campaign;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CampaignSummary {
    private long totalFeedbacks;
    private List<QuestionSummary> questionSummaries;

    public CampaignSummary(Campaign campaign) {
        this.setQuestionSummaries(
                campaign.getQuestions()
                        .stream()
                        .map(question -> new QuestionSummary(question, campaign.getFeedbacks()))
                        .collect(Collectors.toList())
        );
        this.totalFeedbacks = campaign.getFeedbacks().size();
    }
}
