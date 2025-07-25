package com.edu.pwc.forum.api.dtos;

import java.util.Date;

public record TopicResponse(
        Long id,
        String title,
        Date createdOn,
        Date modifiedOn,
        String authorUsername,
        CategoryResponse category,
        String body,
        Long upvotes,
        Long downvotes,
        Long voteScore
) {
}
