package com.edu.pwc.forum.api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicRequest {
    @NotBlank(message = "Title must not be null nor empty")
    String title;
    @NotBlank(message = "Username must not be null nor empty")
    String username;
}
