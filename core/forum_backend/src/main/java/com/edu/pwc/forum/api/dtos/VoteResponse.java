package com.edu.pwc.forum.api.dtos;

public record VoteResponse(
        Long id,
        String username,
        Long topicId,
        Long replyId,
        Boolean isUpvote,
        java.util.Date createdOn
) {
}
