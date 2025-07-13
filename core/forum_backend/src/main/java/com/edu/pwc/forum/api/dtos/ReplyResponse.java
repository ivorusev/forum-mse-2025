package com.edu.pwc.forum.api.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyResponse {
    private Long id;
    private Long topicId;
    private Long userId;
    private String content;
    private Date createdOn;
    private Date modifiedOn;
}
