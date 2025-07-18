package com.edu.pwc.forum.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "topic_id"}),
    @UniqueConstraint(columnNames = {"user_id", "reply_id"})
})
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private ReplyEntity reply;

    @Column(nullable = false)
    private Boolean isUpvote; // true for upvote, false for downvote

    @CreationTimestamp
    private Timestamp createdOn;

    // Ensure that a vote is either for a topic OR a reply, not both
    @PrePersist
    @PreUpdate
    private void validateVote() {
        if ((topic == null && reply == null) || (topic != null && reply != null)) {
            throw new IllegalStateException("Vote must be for either a topic or a reply, not both or neither");
        }
    }
}
