package com.edu.pwc.forum.api.dtos;

import java.util.Date;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Date createdOn;
    private Date modifiedOn;
}