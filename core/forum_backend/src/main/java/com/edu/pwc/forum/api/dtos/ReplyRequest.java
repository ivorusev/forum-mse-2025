package com.edu.pwc.forum.api.dtos;

import lombok.Data;

@Data
public class ReplyRequest {
    private String topicTitle;
    private String username;
    private String replyBody;
}