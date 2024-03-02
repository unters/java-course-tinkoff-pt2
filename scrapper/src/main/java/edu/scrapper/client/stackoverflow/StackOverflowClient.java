package edu.scrapper.client.stackoverflow;

import edu.scrapper.client.stackoverflow.dto.AnswerTo;
import edu.scrapper.client.stackoverflow.dto.CommentTo;
import edu.scrapper.client.stackoverflow.dto.ItemsTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface StackOverflowClient {

    /**
     * Get list of comments to specified questions.
     *
     * @param questionIds List of semicolon delimited question ids (up to 100).
     */
    default ItemsTo<CommentTo> getQuestionComments(String questionIds) {
        return getQuestionComments(questionIds, null);
    }

    /**
     * Get list of answers to specified questions.
     *
     * @param questionIds List of semicolon delimited question ids (up to 100).
     */
    default ItemsTo<AnswerTo> getQuestionAnswers(String questionIds) {
        return getQuestionAnswers(questionIds, null);
    }

    /**
     * Get list of comments to specified answers.
     *
     * @param answerIds List of semicolon delimited answer ids (up to 100).
     */
    default ItemsTo<CommentTo> getAnswerComments(String answerIds) {
        return getAnswerComments(answerIds, null);
    }

    /**
     * Get list of comments to specified questions.
     *
     * @param questionIds    List of semicolon delimited question ids (up to 100).
     * @param lastUpdateTime Timestamp in POSIX-format (Unix-time). If specified, only results that were last updated
     *                       after the given time will be returned.
     */
    @GetExchange("/questions/{ids}/comments")
    ItemsTo<CommentTo> getQuestionComments(
        @PathVariable("ids")
        String questionIds,
        @RequestParam(name = "fromdate", required = false)
        Long lastUpdateTime
    );

    /**
     * Get list of answers to specified questions.
     *
     * @param questionIds    List of semicolon delimited question ids (up to 100).
     * @param lastUpdateTime Timestamp in POSIX-format (Unix-time). If specified, only results that were last updated
     *                       after the given time will be returned.
     */
    @GetExchange("/questions/{ids}/answers")
    ItemsTo<AnswerTo> getQuestionAnswers(
        @PathVariable("ids")
        String questionIds,
        @RequestParam(name = "fromdate", required = false)
        Long lastUpdateTime
    );

    /**
     * Get list of comments to specified answers.
     *
     * @param answerIds      List of semicolon delimited answer ids (up to 100).
     * @param lastUpdateTime Timestamp in POSIX-format (Unix-time). If specified, only results that were last updated
     *                       after the given time will be returned.
     */
    @GetExchange("/answers/{ids}/comments")
    ItemsTo<CommentTo> getAnswerComments(
        @PathVariable("ids")
        String answerIds,
        @RequestParam(name = "fromdate", required = false)
        Long lastUpdateTime
    );
}
