package com.epam.k8scourse.postservice.service;

import com.epam.k8scourse.postservice.dto.PostResponse;
import com.epam.k8scourse.postservice.model.Post;
import com.epam.k8scourse.postservice.repository.PostRepository;
import com.epam.k8scourse.postservice.dto.PostRequest;
import com.epam.k8scourse.postservice.dto.UserRequest;
import com.epam.k8scourse.postservice.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${user.service.base.path}")
    private String userServiceBasePath;

    private final PostRepository postRepository;
    private final WebClient webClient;

    public PostResponse createPost(PostRequest postRequest) {
        if (Objects.nonNull(postRequest.getAuthorId()) && exist(postRequest.getText())) {
            try {
                return getUserResponse(postRequest.getAuthorId())
                        .map((userResponse) -> createPost(userResponse, postRequest)).get();
            } catch (WebClientResponseException ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public PostResponse getPost(Long id) {
        return postRepository.findById(id)
                .map(PostService::mapToPostResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deletePost(Long id) {
        postRepository.findById(id)
                .ifPresentOrElse(this::deletePost,
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                        }
                );
    }

    private void deletePost(Post post) {
        postRepository.deleteById(post.getId());
        UserResponse userResponse = getUserResponse(post.getAuthorId()).get();
        UserRequest userRequest = createUserRequest(userResponse, decreaseByOne);
        updatedUserAmountOfPost(post.getAuthorId(), userRequest);
    }

    public PostResponse updatePost(Long id, PostRequest postRequest) {
        return postRepository.findById(id)
                .map(currentPost -> updatePost(currentPost, postRequest))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Optional<UserResponse> getUserResponse(Long id) {
        return Optional.ofNullable(Objects.requireNonNull(webClient.get()
                .uri("http://user-service:8080/users/{id}", id)
                .retrieve()
                .toEntity(UserResponse.class)
                .block()).getBody());
    }

    private PostResponse createPost(UserResponse userResponse, PostRequest postRequest) {
        if (exist(postRequest.getText())) {
            Post post = Post.builder()
                    .authorId(userResponse.getId())
                    .text(postRequest.getText())
                    .postedAt(LocalDate.now())
                    .build();
            postRepository.save(post);
            UserRequest userRequest = createUserRequest(userResponse, increaseByOne);
            updatedUserAmountOfPost(postRequest.getAuthorId(), userRequest);
            return mapToPostResponse(post);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void updatedUserAmountOfPost(Long id, UserRequest userRequest) {
        webClient.put()
                .uri(getUserServiceUrl(id))
                .bodyValue(userRequest)
                .retrieve()
                .toEntity(UserResponse.class)
                .block();
    }

    private static UserRequest createUserRequest(UserResponse userResponse, Function<UserResponse, String> function) {
        return UserRequest.builder()
                .username(userResponse.getUsername())
                .amountOfPosts(changeAmountOfPost(userResponse, function))
                .build();
    }

    private static String changeAmountOfPost(UserResponse userResponse, Function<UserResponse, String> function) {
        return function.apply(userResponse);
    }

    private static final Function<UserResponse, String> increaseByOne =
            (userResponse) -> String.valueOf(Integer.parseInt(userResponse.getAmountOfPosts()) + 1);

    private static final Function<UserResponse, String> decreaseByOne =
            (userResponse) -> String.valueOf(Integer.parseInt(userResponse.getAmountOfPosts()) - 1);


    private static PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthorId())
                .text(post.getText())
                .postedAt(post.getPostedAt())
                .build();
    }

    private PostResponse updatePost(Post currentPost, PostRequest postRequest) {
        if (exist(postRequest.getText())) {
            Post updatedPost = Post.builder()
                    .id(currentPost.getId())
                    .authorId(currentPost.getAuthorId())
                    .text(postRequest.getText())
                    .postedAt(LocalDate.now())
                    .build();
            postRepository.save(updatedPost);
            return mapToPostResponse(updatedPost);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private static boolean exist(String value) {
        return !Objects.isNull(value) && !value.isBlank();
    }

    private String getUserServiceUrl(Long userId) {
        return userServiceBasePath + "/users/" + userId;
    }
}
