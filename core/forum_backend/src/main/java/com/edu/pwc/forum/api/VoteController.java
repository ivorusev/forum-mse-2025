package com.edu.pwc.forum.api;

import com.edu.pwc.forum.api.dtos.VoteRequest;
import com.edu.pwc.forum.api.dtos.VoteResponse;
import com.edu.pwc.forum.service.services.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteResponse> castVote(@RequestBody VoteRequest voteRequest) {
        VoteResponse response = voteService.castVote(voteRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<Integer> getVoteCount(@PathVariable Long id) {
        int count = (int) voteService.getVoteCount(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/user/{userId}/item/{itemId}")
    public ResponseEntity<String> getUserVote(@PathVariable Long userId, @PathVariable Long itemId) {
        String userVote = voteService.getUserVote(userId, itemId);
        return ResponseEntity.ok(userVote != null ? userVote : "none");
    }
}