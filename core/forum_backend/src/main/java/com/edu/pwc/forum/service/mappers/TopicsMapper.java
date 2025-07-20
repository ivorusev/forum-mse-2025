package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.service.services.VoteService;

public class TopicsMapper {

    public static TopicResponse mapToDTO(TopicEntity topic, CategoryMapper categoryMapper) {
        return new TopicResponse(
            topic.getId(), 
            topic.getTitle(), 
            topic.getCreatedOn(), 
            topic.getModifiedOn(), 
            topic.getUser().getUsername(),
            topic.getCategory() != null ? categoryMapper.toDto(topic.getCategory()) : null,
            topic.getBody(),
            0L, // upvotes - will be updated by service
            0L, // downvotes - will be updated by service  
            0L  // voteScore - will be updated by service
        );
    }

    public static TopicResponse mapToDTOWithVotes(TopicEntity topic, VoteService voteService, CategoryMapper categoryMapper) {
        Long upvotes = voteService.getTopicUpvotes(topic.getId());
        Long downvotes = voteService.getTopicDownvotes(topic.getId());
        Long voteScore = voteService.getTopicVoteScore(topic.getId());
        
        return new TopicResponse(
            topic.getId(), 
            topic.getTitle(), 
            topic.getCreatedOn(), 
            topic.getModifiedOn(), 
            topic.getUser().getUsername(),
            topic.getCategory() != null ? categoryMapper.toDto(topic.getCategory()) : null,
            topic.getBody(),
            upvotes,
            downvotes,
            voteScore
        );
    }
}
