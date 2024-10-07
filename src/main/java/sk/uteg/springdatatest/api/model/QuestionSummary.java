package sk.uteg.springdatatest.api.model;

import lombok.Data;
import sk.uteg.springdatatest.db.model.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Data
public class QuestionSummary {
    private String name;
    private QuestionType type;

    // will be ZERO for type == CHOICE
    private BigDecimal ratingAverage;

    // will be empty for type == RATING
    private List<OptionSummary> optionSummaries;

    public QuestionSummary(Question question, List<Feedback> feedbacks) {
        name = question.getText();
        type = question.getType();

        List<Answer> answers = getQuestionAnswersFromFeedback(question, feedbacks);

        ratingAverage = getQuestionRatingAverage(question, answers);
        optionSummaries = getQuestionOptionSummaries(question, answers);
    }

    // gets answers for given question from campaign feedbacks
    private List<Answer> getQuestionAnswersFromFeedback(Question question, List<Feedback> feedbacks) {
        return feedbacks.stream()
                .flatMap(feedback -> feedback.getAnswers()
                        .stream()
                        .filter(answer -> answer.getQuestion()
                                .getId()
                                .equals(question.getId()))
                ).collect(Collectors.toList());
    }

    // calculates the average rating if question of type rating
    private BigDecimal getQuestionRatingAverage(Question question, List<Answer> answers) {
            if(!answers.isEmpty() && question.getType().equals(QuestionType.RATING)) {
                return BigDecimal.valueOf(answers.stream().mapToInt(Answer::getRatingValue).sum())
                        .divide(BigDecimal.valueOf(answers.size()), MathContext.DECIMAL32);
            }

            return BigDecimal.ZERO;
    }

    // summarizes the occurrences per option if question of type choice
    public List<OptionSummary> getQuestionOptionSummaries(Question question, List<Answer> answers) {
        if(question.getType().equals(QuestionType.CHOICE)) {
            // group options by occurrences
            Map<Option, Long> optionOccurrences = answers.stream()
                    .flatMap(answer -> answer.getSelectedOptions().stream())
                    .collect(
                            Collectors.groupingBy(Function.identity(), Collectors.counting())
                    );

            return question.getOptions()
                    .stream()
                    .map(option -> new OptionSummary(option.getText(), Math.toIntExact(optionOccurrences.getOrDefault(option, 0L))))
                    .collect(toList());
        }

        return Collections.emptyList();
    }
}
