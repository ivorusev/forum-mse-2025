package com.edu.pwc.forum.api;

import com.edu.pwc.forum.api.dtos.ReplyRequest;
import com.edu.pwc.forum.api.dtos.ReplyResponse;
import com.edu.pwc.forum.service.services.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ReplyResponse> createReply(@RequestBody ReplyRequest request) {
        return ResponseEntity.ok().body(replyService.save(request));
    }

    @GetMapping("/topic/{topicId}")
    public Page<ReplyResponse> getRepliesByTopic(
            @PathVariable Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return replyService.getRepliesByTopicId(topicId, Pageable.ofSize(size).withPage(page));
    }
}