package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.ReplyRequest;
import com.edu.pwc.forum.api.dtos.ReplyResponse;
import com.edu.pwc.forum.persistence.entity.ReplyEntity;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.entity.UserEntity;
import com.edu.pwc.forum.persistence.repositories.ReplyRepository;
import com.edu.pwc.forum.service.mappers.ReplyMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ReplyMapper replyMapper;
    private final UserService userService;
    private final TopicService topicService;
    private final VoteService voteService;

    public ReplyResponse createReply(ReplyRequest request) {
        TopicEntity topic = topicService.findByTitle(request.getTopicTitle());
        ReplyEntity reply = replyMapper.requestToEntity(request);
        UserEntity author = userService.findByUsername(request.getUsername());
        author.getReplies().add(reply);
        reply.setTopic(topic);
        reply.setUser(author);
        ReplyEntity replyEntity = replyRepository.save(reply);
        return mapToResponseWithVotes(replyEntity);
    }

    private ReplyResponse mapToResponseWithVotes(ReplyEntity entity) {
        ReplyResponse response = replyMapper.entityToResponse(entity);
        response.setUpvotes(voteService.getReplyUpvotes(entity.getId()));
        response.setDownvotes(voteService.getReplyDownvotes(entity.getId()));
        response.setVoteScore(voteService.getReplyVoteScore(entity.getId()));
        return response;
    }

    /**
     * @param topicId
     * @param pageable
     * @return
     */
    public Page<ReplyResponse> getRepliesByTopicId(Long topicId, Pageable pageable) {
        return replyRepository.findByTopicId(topicId, pageable)
                .map(this::mapToResponseWithVotes);
    }

    @Transactional
    public void deleteById(Long id) {
        replyRepository.findById(id).ifPresent(reply -> {
            reply.getUser().getReplies().remove(reply);
            reply.getTopic().getReplies().remove(reply);
            replyRepository.delete(reply);
        });
    }
}