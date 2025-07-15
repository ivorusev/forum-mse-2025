package com.edu.pwc.forum.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "reply_entity")
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long topicId;

    private Long userId;

    @Column(nullable = false, columnDefinition = "text")
    private String replyBody;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date modifiedOn;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private TopicEntity topicEntity;
}
