package com.edu.pwc.forum.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "topic_entity", schema = "forum")
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

    @OneToMany(mappedBy = "topicEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReplyEntity> replies;
}
