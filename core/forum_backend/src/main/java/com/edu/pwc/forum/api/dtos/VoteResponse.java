package com.edu.pwc.forum.api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {
    private Long topicOrReplyId;
    private int voteCount;
}