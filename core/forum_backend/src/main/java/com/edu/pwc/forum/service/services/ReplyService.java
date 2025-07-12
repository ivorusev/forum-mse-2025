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

    public ReplyResponse createReply(ReplyRequest request) {
        TopicEntity topic = topicService.findByTitle(request.getTopicTitle());
        ReplyEntity reply = replyMapper.requestToEntity(request);
        UserEntity author = userService.findByUsername(request.getUsername());
        author.getReplies().add(reply);
        reply.setTopic(topic);
        reply.setUser(author);
        ReplyEntity replyEntity = replyRepository.save(reply);
        return replyMapper.entityToResponse(replyEntity);
    }

    /**
     * @param topicId
     * @param pageable
     * @return
     */
    public Page<ReplyResponse> getRepliesByTopicId(Long topicId, Pageable pageable) {
        return replyRepository.findByTopicId(topicId, pageable)
                .map(replyMapper::entityToResponse);
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