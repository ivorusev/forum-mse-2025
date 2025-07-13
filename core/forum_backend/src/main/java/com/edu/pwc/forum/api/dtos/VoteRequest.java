package com.edu.pwc.forum.api.dtos;

import lombok.Data;

@Data
public class VoteRequest {
    private Long userId; // <-- Add this field!
    private Long topicOrReplyId;
    private String voteType; // "upvote" or "downvote"
}