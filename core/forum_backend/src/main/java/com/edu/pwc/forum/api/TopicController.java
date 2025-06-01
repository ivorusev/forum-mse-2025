package com.edu.pwc.forum.api;

import com.edu.pwc.forum.api.dtos.PageResult;
import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.service.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "http://localhost:3000", allowCredentials = "true")
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public void createTopic(@RequestBody TopicRequest request) {
        System.out.println(request);
        topicService.save(request);
    }

    @Operation(summary = "Get all topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topics were successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<PageResult<TopicResponse>> getAllTopics(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(topicService.getAllTopics(page, size));
    }


}