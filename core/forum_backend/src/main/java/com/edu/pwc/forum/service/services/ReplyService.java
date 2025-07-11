package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.ReplyRequest;
import com.edu.pwc.forum.api.dtos.ReplyResponse;
import com.edu.pwc.forum.persistence.entity.ReplyEntity;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.repositories.ReplyRepository;
import com.edu.pwc.forum.service.mappers.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ReplyMapper replyMapper;

    private final TopicService topicService;

    public ReplyResponse save(ReplyRequest request) {
        TopicEntity topic = topicService.findByTitle(request.getTopicTitle());
        ReplyEntity entity = replyMapper.requestToEntity(request);
        entity.setTopicEntity(topic);
        entity.setTopicId(topic.getId());
        ReplyEntity replyEntity = replyRepository.save(entity);
        return replyMapper.entityToResponse(replyEntity);
    }

	/**
	 *
	 * @param topicId
	 * @param pageable
	 * @return
	 */
	public Page<ReplyResponse> getRepliesByTopicId(Long topicId, Pageable pageable) {
		return replyRepository.findByTopicId(topicId, pageable)
				.map(replyMapper::entityToResponse);
	}
}