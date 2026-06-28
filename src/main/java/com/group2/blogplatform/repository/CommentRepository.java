package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.Comment;
import com.group2.blogplatform.entity.StatusComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            select c
            from Comment c
            where c.post.id = :postId and c.statusComment = :status
            order by c.createdAt asc
            """)
    List<Comment> findVisibleByPostId(@Param("postId") Long postId, @Param("status") StatusComment status);

    long countByPost_IdAndStatusComment(Long postId, StatusComment statusComment);
}
