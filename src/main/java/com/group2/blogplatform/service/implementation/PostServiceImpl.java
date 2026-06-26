package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.request.CreatePostRequest;
import com.group2.blogplatform.dto.response.CreatePostResponse;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.StatusPost;
import com.group2.blogplatform.entity.Topic;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.TopicRepository;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;


    @Override
    public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
        // Hard code vi chua co login
        User user = userRepository.findByID(1L);
        if (user == null) {
            return new CreatePostResponse(false, "User not found");
        }

        Topic topic = topicRepository.findByID(createPostRequest.getTopicId());

        Post post = new Post().builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .imageUrl(createPostRequest.getImageUrl())
                .statusPost(StatusPost.PUBLISHED)
                .topic(topic)
                .user(user)
                .build();
        postRepository.save(post);
        return new CreatePostResponse(true, "Post created successfully");
    }
}
