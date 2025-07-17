package com.edu.pwc.forum.api.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyResponse {
    private Long id;
    private String topicTitle;

    private String authorUsername;

    private String replyBody;

    private Date createdOn;

    private Date modifiedOn;
}
