package com.edu.pwc.forum.api;

import com.edu.pwc.forum.api.dtos.ReplyRequest;
import com.edu.pwc.forum.api.dtos.ReplyResponse;
import com.edu.pwc.forum.service.services.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "Create a reply for a topic")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reply created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reply data")
    })
    @PostMapping
    public ResponseEntity<ReplyResponse> createReply(@RequestBody ReplyRequest request) {
        return ResponseEntity.ok().body(replyService.createReply(request));
    }

    @Operation(summary = "Get replies by topic ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Replies retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @GetMapping("/topic/{topicId}")
    public Page<ReplyResponse> getRepliesByTopic(
            @PathVariable Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return replyService.getRepliesByTopicId(topicId, Pageable.ofSize(size).withPage(page));
    }

    @Operation(summary = "Delete reply by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reply deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Reply not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable("id") Long id) {
        replyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}