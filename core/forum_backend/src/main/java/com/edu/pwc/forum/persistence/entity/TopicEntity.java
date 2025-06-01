package com.edu.pwc.forum.persistence.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
public class TopicEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true, nullable = false)
	private String title;

	@CreationTimestamp
	private Timestamp createdOn;

	@UpdateTimestamp
	private Timestamp modifiedOn;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "topicEntity", cascade = CascadeType.ALL)
	private List<ReplyEntity> replies;
}
