package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.VoteRequest;
import com.edu.pwc.forum.api.dtos.VoteResponse;
import com.edu.pwc.forum.exception.ResourceNotFoundException;
import com.edu.pwc.forum.persistence.entity.*;
import com.edu.pwc.forum.persistence.repositories.*;
import com.edu.pwc.forum.service.mappers.VoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final ReplyRepository replyRepository;
    private final VoteMapper voteMapper;

    @Transactional
    public VoteResponse vote(VoteRequest request) {
        // Validate that either topicId or replyId is provided, but not both
        if ((request.getTopicId() == null && request.getReplyId() == null) ||
            (request.getTopicId() != null && request.getReplyId() != null)) {
            throw new IllegalArgumentException("Vote must be for either a topic or a reply, not both or neither");
        }

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + request.getUsername()));

        VoteEntity existingVote = null;
        TopicEntity topic = null;
        ReplyEntity reply = null;

        if (request.getTopicId() != null) {
            topic = topicRepository.findById(request.getTopicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + request.getTopicId()));
            existingVote = voteRepository.findByUserAndTopic(user, topic).orElse(null);
        } else {
            reply = replyRepository.findById(request.getReplyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reply not found with ID: " + request.getReplyId()));
            existingVote = voteRepository.findByUserAndReply(user, reply).orElse(null);
        }

        VoteEntity vote;
        
        if (existingVote != null) {
            // If user clicks the same vote type, remove the vote (toggle off)
            if (existingVote.getIsUpvote().equals(request.getIsUpvote())) {
                voteRepository.delete(existingVote);
                return null; // Indicate vote was removed
            } else {
                // Change vote type (upvote to downvote or vice versa)
                existingVote.setIsUpvote(request.getIsUpvote());
                vote = voteRepository.save(existingVote);
            }
        } else {
            // Create new vote
            vote = new VoteEntity();
            vote.setUser(user);
            vote.setTopic(topic);
            vote.setReply(reply);
            vote.setIsUpvote(request.getIsUpvote());
            vote = voteRepository.save(vote);
        }

        return voteMapper.entityToResponse(vote);
    }

    public Long getTopicVoteScore(Long topicId) {
        TopicEntity topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));
        return voteRepository.getTopicVoteScore(topic);
    }

    public Long getReplyVoteScore(Long replyId) {
        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found with ID: " + replyId));
        return voteRepository.getReplyVoteScore(reply);
    }

    public Long getTopicUpvotes(Long topicId) {
        TopicEntity topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));
        return voteRepository.countUpvotesByTopic(topic);
    }

    public Long getTopicDownvotes(Long topicId) {
        TopicEntity topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));
        return voteRepository.countDownvotesByTopic(topic);
    }

    public Long getReplyUpvotes(Long replyId) {
        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found with ID: " + replyId));
        return voteRepository.countUpvotesByReply(reply);
    }

    public Long getReplyDownvotes(Long replyId) {
        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found with ID: " + replyId));
        return voteRepository.countDownvotesByReply(reply);
    }

    public VoteResponse getUserTopicVote(Long topicId, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        TopicEntity topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));
        
        return voteRepository.findByUserAndTopic(user, topic)
                .map(voteMapper::entityToResponse)
                .orElse(null);
    }

    public VoteResponse getUserReplyVote(Long replyId, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found with ID: " + replyId));
        
        return voteRepository.findByUserAndReply(user, reply)
                .map(voteMapper::entityToResponse)
                .orElse(null);
    }
}
