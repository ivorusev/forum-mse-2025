package com.edu.pwc.forum.persistence.repositories;

import com.edu.pwc.forum.persistence.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    @Query("SELECT COUNT(v) FROM VoteEntity v WHERE v.topicOrReplyId = :topicOrReplyId")
    long countByTopicOrReplyId(@Param("topicOrReplyId") Long topicOrReplyId);

    @Query("SELECT v FROM VoteEntity v WHERE v.userId = :userId AND v.topicOrReplyId = :topicOrReplyId")
    Optional<VoteEntity> findByUserIdAndTopicOrReplyId(@Param("userId") Long userId, @Param("topicOrReplyId") Long topicOrReplyId);

    @Query("SELECT SUM(CASE WHEN v.voteType = com.edu.pwc.forum.persistence.entity.VoteType.UPVOTE THEN 1 WHEN v.voteType = com.edu.pwc.forum.persistence.entity.VoteType.DOWNVOTE THEN -1 ELSE 0 END) FROM VoteEntity v WHERE v.topicOrReplyId = :id")
    Integer getNetVoteCount(@Param("id") Long id);
}