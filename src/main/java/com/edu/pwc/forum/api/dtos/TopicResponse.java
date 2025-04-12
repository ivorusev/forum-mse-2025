package com.edu.pwc.forum.api.dtos;

import java.util.Date;

public record TopicResponse(
        String title,

        Date createdOn,

        Date modifiedOn
) { }
