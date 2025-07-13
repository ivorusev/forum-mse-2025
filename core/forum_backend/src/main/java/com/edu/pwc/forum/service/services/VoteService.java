package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.VoteRequest;
import com.edu.pwc.forum.api.dtos.VoteResponse;
import com.edu.pwc.forum.persistence.entity.VoteEntity;
import com.edu.pwc.forum.persistence.entity.VoteType;
import com.edu.pwc.forum.persistence.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteResponse castVote(VoteRequest request) {
        log.info("Casting vote: userId={}, topicOrReplyId={}, voteType={}", 
                request.getUserId(), request.getTopicOrReplyId(), request.getVoteType());
                
        Optional<VoteEntity> existingVoteOpt = voteRepository.findByUserIdAndTopicOrReplyId(request.getUserId(), request.getTopicOrReplyId());
        VoteEntity voteEntity;

        // Convert string vote type to enum
        VoteType voteType = convertStringToVoteType(request.getVoteType());

        if (existingVoteOpt.isPresent()) {
            log.info("Found existing vote for user {}", request.getUserId());
            voteEntity = existingVoteOpt.get();

            // If user clicks the same vote type, remove the vote (toggle off)
            if (voteEntity.getVoteType().equals(voteType)) {
                log.info("Removing vote (toggle off)");
                voteRepository.delete(voteEntity);
            } else {
                // Switch to the opposite vote
                log.info("Switching vote from {} to {}", voteEntity.getVoteType(), voteType);
                voteEntity.setVoteType(voteType);
                voteRepository.save(voteEntity);
            }
        } else {
            // Create new vote
            log.info("Creating new vote for user {}", request.getUserId());
            voteEntity = new VoteEntity();
            voteEntity.setUserId(request.getUserId());
            voteEntity.setTopicOrReplyId(request.getTopicOrReplyId());
            voteEntity.setVoteType(voteType);
            voteRepository.save(voteEntity);
        }

        Integer netCount = voteRepository.getNetVoteCount(request.getTopicOrReplyId());
        log.info("Final vote count for item {}: {}", request.getTopicOrReplyId(), netCount);
        return new VoteResponse(request.getTopicOrReplyId(), netCount != null ? netCount : 0);
    }

    public long getVoteCount(Long topicOrReplyId) {
        Integer netCount = voteRepository.getNetVoteCount(topicOrReplyId);
        return netCount != null ? netCount : 0;
    }

    public String getUserVote(Long userId, Long topicOrReplyId) {
        Optional<VoteEntity> voteOpt = voteRepository.findByUserIdAndTopicOrReplyId(userId, topicOrReplyId);
        if (voteOpt.isPresent()) {
            VoteType voteType = voteOpt.get().getVoteType();
            return voteType == VoteType.UPVOTE ? "upvote" : "downvote";
        }
        return null;
    }

    private VoteType convertStringToVoteType(String voteTypeStr) {
        if ("upvote".equalsIgnoreCase(voteTypeStr)) {
            return VoteType.UPVOTE;
        } else if ("downvote".equalsIgnoreCase(voteTypeStr)) {
            return VoteType.DOWNVOTE;
        } else {
            throw new IllegalArgumentException("Invalid vote type: " + voteTypeStr);
        }
    }
}