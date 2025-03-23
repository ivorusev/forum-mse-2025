package com.edu.pwc.forum.api.dtos;

import java.util.Date;
import lombok.Data;

@Data
public class TopicResponse {

	private Long id;

	private String title;

	private Date createdOn;

	private Date modifiedOn;
}
