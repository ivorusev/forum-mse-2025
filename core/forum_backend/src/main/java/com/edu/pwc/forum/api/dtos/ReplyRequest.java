package com.edu.pwc.forum.api.dtos;

import lombok.Data;

@Data
public class ReplyRequest {
    private Long topicId;
    private Long userId;
    private String content;
}