package com.edu.pwc.forum.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {
    @NotBlank(message = "Username must not be null nor empty")
    private String username;
    
    private Long topicId;
    
    private Long replyId;
    
    @NotNull(message = "Vote type must be specified")
    private Boolean isUpvote; // true for upvote, false for downvote
}
