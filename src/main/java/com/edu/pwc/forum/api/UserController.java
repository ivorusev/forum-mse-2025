package com.edu.pwc.forum.api;

import org.springframework.web.bind.annotation.*;
import com.edu.pwc.forum.api.dtos.UserRequest;
import com.edu.pwc.forum.service.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public void createUser(@RequestBody UserRequest request) {
        userService.createUser(request);
    }
}