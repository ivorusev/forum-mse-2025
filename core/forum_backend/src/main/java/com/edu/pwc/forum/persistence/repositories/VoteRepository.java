package com.edu.pwc.forum.persistence.repositories;

import com.edu.pwc.forum.persistence.entity.VoteEntity;
import com.edu.pwc.forum.persistence.entity.UserEntity;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    // Find vote by user and topic
    Optional<VoteEntity> findByUserAndTopic(UserEntity user, TopicEntity topic);

    // Find vote by user and reply
    Optional<VoteEntity> findByUserAndReply(UserEntity user, ReplyEntity reply);

    // Count upvotes for a topic
    @Query("SELECT COUNT(v) FROM VoteEntity v WHERE v.topic = :topic AND v.isUpvote = true")
    Long countUpvotesByTopic(@Param("topic") TopicEntity topic);

    // Count downvotes for a topic
    @Query("SELECT COUNT(v) FROM VoteEntity v WHERE v.topic = :topic AND v.isUpvote = false")
    Long countDownvotesByTopic(@Param("topic") TopicEntity topic);

    // Count upvotes for a reply
    @Query("SELECT COUNT(v) FROM VoteEntity v WHERE v.reply = :reply AND v.isUpvote = true")
    Long countUpvotesByReply(@Param("reply") ReplyEntity reply);

    // Count downvotes for a reply
    @Query("SELECT COUNT(v) FROM VoteEntity v WHERE v.reply = :reply AND v.isUpvote = false")
    Long countDownvotesByReply(@Param("reply") ReplyEntity reply);

    // Get vote score (upvotes - downvotes) for a topic
    @Query("SELECT COALESCE(SUM(CASE WHEN v.isUpvote = true THEN 1 ELSE -1 END), 0) FROM VoteEntity v WHERE v.topic = :topic")
    Long getTopicVoteScore(@Param("topic") TopicEntity topic);

    // Get vote score (upvotes - downvotes) for a reply
    @Query("SELECT COALESCE(SUM(CASE WHEN v.isUpvote = true THEN 1 ELSE -1 END), 0) FROM VoteEntity v WHERE v.reply = :reply")
    Long getReplyVoteScore(@Param("reply") ReplyEntity reply);
}
