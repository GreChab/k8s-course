package com.epam.k8scourse.postservice.controller;

import com.epam.k8scourse.postservice.dto.PostRequest;
import com.epam.k8scourse.postservice.dto.PostResponse;
import com.epam.k8scourse.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        PostResponse postResponse = postService.createPost(postRequest);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id) {
        PostResponse postResponse = postService.getPost(id);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable("id") Long id, @RequestBody PostRequest postRequest) {
        PostResponse postResponse = postService.updatePost(id, postRequest);
        return ResponseEntity.ok(postResponse);
    }
}
