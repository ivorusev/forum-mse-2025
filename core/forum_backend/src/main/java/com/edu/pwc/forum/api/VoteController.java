package com.edu.pwc.forum.api;

import com.edu.pwc.forum.api.dtos.VoteRequest;
import com.edu.pwc.forum.api.dtos.VoteResponse;
import com.edu.pwc.forum.service.services.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@Tag(name = "Vote", description = "Vote management APIs")
public class VoteController {

    private final VoteService voteService;

    @Operation(summary = "Vote on a topic or reply")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vote registered successfully"),
            @ApiResponse(responseCode = "204", description = "Vote removed (toggled off)"),
            @ApiResponse(responseCode = "400", description = "Invalid vote data"),
            @ApiResponse(responseCode = "404", description = "Topic, reply, or user not found")
    })
    @PostMapping
    public ResponseEntity<VoteResponse> vote(@RequestBody @Valid VoteRequest request) {
        VoteResponse response = voteService.vote(request);
        if (response == null) {
            return ResponseEntity.noContent().build(); // Vote was removed
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get vote score for a topic")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vote score retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @GetMapping("/topic/{topicId}/score")
    public ResponseEntity<Long> getTopicVoteScore(@PathVariable Long topicId) {
        Long score = voteService.getTopicVoteScore(topicId);
        return ResponseEntity.ok(score);
    }

    @Operation(summary = "Get vote score for a reply")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vote score retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Reply not found")
    })
    @GetMapping("/reply/{replyId}/score")
    public ResponseEntity<Long> getReplyVoteScore(@PathVariable Long replyId) {
        Long score = voteService.getReplyVoteScore(replyId);
        return ResponseEntity.ok(score);
    }

    @Operation(summary = "Get upvote count for a topic")
    @GetMapping("/topic/{topicId}/upvotes")
    public ResponseEntity<Long> getTopicUpvotes(@PathVariable Long topicId) {
        Long upvotes = voteService.getTopicUpvotes(topicId);
        return ResponseEntity.ok(upvotes);
    }

    @Operation(summary = "Get downvote count for a topic")
    @GetMapping("/topic/{topicId}/downvotes")
    public ResponseEntity<Long> getTopicDownvotes(@PathVariable Long topicId) {
        Long downvotes = voteService.getTopicDownvotes(topicId);
        return ResponseEntity.ok(downvotes);
    }

    @Operation(summary = "Get upvote count for a reply")
    @GetMapping("/reply/{replyId}/upvotes")
    public ResponseEntity<Long> getReplyUpvotes(@PathVariable Long replyId) {
        Long upvotes = voteService.getReplyUpvotes(replyId);
        return ResponseEntity.ok(upvotes);
    }

    @Operation(summary = "Get downvote count for a reply")
    @GetMapping("/reply/{replyId}/downvotes")
    public ResponseEntity<Long> getReplyDownvotes(@PathVariable Long replyId) {
        Long downvotes = voteService.getReplyDownvotes(replyId);
        return ResponseEntity.ok(downvotes);
    }

    @Operation(summary = "Get user's vote for a topic")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User vote retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Topic or user not found, or user hasn't voted")
    })
    @GetMapping("/topic/{topicId}/user/{username}")
    public ResponseEntity<VoteResponse> getUserTopicVote(@PathVariable Long topicId, @PathVariable String username) {
        VoteResponse vote = voteService.getUserTopicVote(topicId, username);
        if (vote == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vote);
    }

    @Operation(summary = "Get user's vote for a reply")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User vote retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Reply or user not found, or user hasn't voted")
    })
    @GetMapping("/reply/{replyId}/user/{username}")
    public ResponseEntity<VoteResponse> getUserReplyVote(@PathVariable Long replyId, @PathVariable String username) {
        VoteResponse vote = voteService.getUserReplyVote(replyId, username);
        if (vote == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vote);
    }
}
