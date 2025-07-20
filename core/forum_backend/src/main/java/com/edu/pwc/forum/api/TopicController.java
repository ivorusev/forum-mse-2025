package com.edu.pwc.forum.api;

import com.edu.pwc.forum.api.dtos.PageResult;
import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.service.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "http://localhost:3000", allowCredentials = "true")
public class TopicController {

    private final TopicService topicService;

    @Operation(
            summary = "Create a new topic",
            description = "Creates a new topic with a unique title"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Topic created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<TopicResponse> createTopic(@RequestBody @Valid TopicRequest request) {
        return new ResponseEntity<>(topicService.createTopic(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get paginated list of topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topics retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PageResult<TopicResponse>> getAllTopics(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") Integer page,

            @Parameter(description = "Number of topics per page", example = "10")
            @RequestParam(required = false, defaultValue = "10") Integer size,

            @Parameter(description = "Category ID to filter by", example = "1")
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(topicService.getAllTopics(page, size, categoryId));
    }

    @Operation(summary = "Delete topic by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Topic deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteTopic(@PathVariable("title") String title) {
        topicService.deleteByTitle(title);
        return ResponseEntity.noContent().build();
    }

}