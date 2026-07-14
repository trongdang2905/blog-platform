package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.response.ToggleLikeResponse;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.PostLike;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.repository.PostLikeRepository;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // TODO: thay bang user dang dang nhap (Spring Security) khi co UC Authentication
    private static final Long CURRENT_USER_ID = 1L;

    @Override
    @Transactional
    public ToggleLikeResponse like(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return new ToggleLikeResponse(false, false, 0, "Post not found");
        }

        boolean alreadyLiked = postLikeRepository.existsByPost_IdAndUser_Id(postId, CURRENT_USER_ID);
        if (!alreadyLiked) {
            User user = userRepository.findByID(CURRENT_USER_ID);
            if (user == null) {
                return new ToggleLikeResponse(false, false, postLikeRepository.countByPost_Id(postId), "User not found");
            }
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUser(user);
            try {
                postLikeRepository.save(like);
            } catch (DataIntegrityViolationException e) {

            }
        }

        long likeCount = postLikeRepository.countByPost_Id(postId);
        return new ToggleLikeResponse(true, true, likeCount, "Liked");
    }

    @Override
    @Transactional
    public ToggleLikeResponse unlike(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return new ToggleLikeResponse(false, false, 0, "Post not found");
        }

        postLikeRepository.deleteByPost_IdAndUser_Id(postId, CURRENT_USER_ID);

        long likeCount = postLikeRepository.countByPost_Id(postId);
        return new ToggleLikeResponse(true, false, likeCount, "Unliked");
    }

    @Override
    public long countLikes(Long postId) {
        return postLikeRepository.countByPost_Id(postId);
    }

    @Override
    public boolean isLikedByCurrentUser(Long postId) {
        return postLikeRepository.existsByPost_IdAndUser_Id(postId, CURRENT_USER_ID);
    }
}