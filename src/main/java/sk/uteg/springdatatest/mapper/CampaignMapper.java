package sk.uteg.springdatatest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sk.uteg.springdatatest.api.dto.CampaignSummaryDTO;
import sk.uteg.springdatatest.api.dto.QuestionSummaryDTO;
import sk.uteg.springdatatest.db.model.Answer;
import sk.uteg.springdatatest.db.model.Campaign;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Mapper(componentModel = "spring")
public interface CampaignMapper {
    CampaignMapper INSTANCE = Mappers.getMapper(CampaignMapper.class);

    @Mapping(target = "totalFeedbacks", expression = "java(getTotalFeedbacks(campaign))")
    @Mapping(target = "questionSummaries", expression = "java(getQuestionSummaries(campaign))")
    CampaignSummaryDTO toCampaignSummaryDTO(Campaign campaign);

    /**
     * Counts Feedbacks for given Campaign
     *
     * @param campaign
     * @return
     */
    default long getTotalFeedbacks(Campaign campaign) {
        return emptyIfNull(campaign.getFeedbacks()).size();
    }

    /**
     * Maps campaign questions into QuestionSummaryDTO
     *
     * @param campaign
     * @return
     */
    default List<QuestionSummaryDTO> getQuestionSummaries(Campaign campaign) {
        return emptyIfNull(campaign.getQuestions())
                .stream()
                .map(question -> {
                    List<Answer> answers = emptyIfNull(campaign.getFeedbacks()).stream()
                            .flatMap(feedback -> emptyIfNull(feedback.getAnswers())
                                    .stream()
                                    .filter(answer -> answer.getQuestion()
                                            .getId()
                                            .equals(question.getId()))
                            ).collect(Collectors.toList());

                    return QuestionMapper.INSTANCE.toQuestionSummaryDTO(question, answers);
                })
                .collect(Collectors.toList());
    }
}
