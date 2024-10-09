package sk.uteg.springdatatest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sk.uteg.springdatatest.api.dto.OptionSummaryDTO;
import sk.uteg.springdatatest.api.dto.QuestionSummaryDTO;
import sk.uteg.springdatatest.db.model.Answer;
import sk.uteg.springdatatest.db.model.Option;
import sk.uteg.springdatatest.db.model.Question;
import sk.uteg.springdatatest.db.model.QuestionType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mapping(target = "name", source = "question.text")
    @Mapping(target = "type", source = "question.type")
    @Mapping(target = "ratingAverage", expression = "java(getRatingAverage(question, answers))")
    @Mapping(target = "optionSummaries", expression = "java(getOptionSummaries(question, answers))")
    QuestionSummaryDTO toQuestionSummaryDTO(Question question, List<Answer> answers);

    /**
     * Calculates the average rating for given Question and Answers if Question is of type RATING, or returns ZERO
     *
     * @param question
     * @param answers
     * @return
     */
    default BigDecimal getRatingAverage(Question question, List<Answer> answers) {
        if(!answers.isEmpty() && question.getType().equals(QuestionType.RATING)) {
            return BigDecimal.valueOf(answers.stream().mapToInt(Answer::getRatingValue).sum())
                    .divide(BigDecimal.valueOf(answers.size()), MathContext.DECIMAL32);
        }

        return BigDecimal.ZERO;
    }

    /**
     * Calculates the occurrences of each Answer Option for given Question if Question is of type CHOICE, or returns empty list
     *
     * @param question
     * @param answers
     * @return
     */
    default List<OptionSummaryDTO> getOptionSummaries(Question question, List<Answer> answers) {
        if(question.getType().equals(QuestionType.CHOICE)) {
            // group options by occurrences
            Map<UUID, Long> optionOccurrences = emptyIfNull(answers).stream()
                    .flatMap(answer -> emptyIfNull(answer.getSelectedOptions()).stream())
                    .collect(
                            Collectors.groupingBy(Option::getId, Collectors.counting())
                    );

            return emptyIfNull(question.getOptions())
                    .stream()
                    .map(option -> OptionMapper.INSTANCE.toOptionSummaryDTO(option, Math.toIntExact(optionOccurrences.getOrDefault(option.getId(), 0L))))
                    .collect(toList());
        }

        return Collections.emptyList();
    }
}
