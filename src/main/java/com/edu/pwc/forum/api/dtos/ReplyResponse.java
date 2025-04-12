package com.edu.pwc.forum.api.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyResponse {
    private String topicTitle;

    private Long userId;

    private String replyBody;

    private Date createdOn;

    private Date modifiedOn;
}
