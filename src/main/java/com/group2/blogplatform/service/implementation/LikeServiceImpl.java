package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.response.ToggleLikeResponse;
import com.group2.blogplatform.entity.Post;
import com.group2.blogplatform.entity.PostLike;
import com.group2.blogplatform.entity.StatusPost;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.repository.PostLikeRepository;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ToggleLikeResponse like(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return new ToggleLikeResponse(false, false, 0, "Post not found");
        }

        if (post.getStatusPost() != StatusPost.PUBLISHED) {
            return new ToggleLikeResponse(false, false, postLikeRepository.countByPost_Id(postId),
                    "This post is no longer available");
        }

        boolean alreadyLiked = postLikeRepository.existsByPost_IdAndUser_Id(postId, userId);
        if (!alreadyLiked) {
            User user = userRepository.findByID(userId);
            if (user == null) {
                return new ToggleLikeResponse(false, false, postLikeRepository.countByPost_Id(postId), "User not found");
            }
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUser(user);
            try {
                postLikeRepository.save(like);
            } catch (DataIntegrityViolationException e) {
                // Da co ban ghi like nay roi (race condition giua 2 request cung luc) -> bo qua, coi nhu da like
                log.debug("Duplicate like ignored for postId={}, userId={}", postId, userId);
            }
        }

        long likeCount = postLikeRepository.countByPost_Id(postId);
        return new ToggleLikeResponse(true, true, likeCount, "Liked");
    }

    @Override
    @Transactional
    public ToggleLikeResponse unlike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return new ToggleLikeResponse(false, false, 0, "Post not found");
        }

        postLikeRepository.deleteByPost_IdAndUser_Id(postId, userId);

        long likeCount = postLikeRepository.countByPost_Id(postId);
        return new ToggleLikeResponse(true, false, likeCount, "Unliked");
    }

    @Override
    public long countLikes(Long postId) {
        return postLikeRepository.countByPost_Id(postId);
    }

    @Override
    public boolean isLikedByCurrentUser(Long postId, Long userId) {
        if (userId == null) {
            return false;
        }
        return postLikeRepository.existsByPost_IdAndUser_Id(postId, userId);
    }
}