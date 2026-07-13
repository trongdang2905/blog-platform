package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
    @Query("""
            select s 
            from SavedPost s 
            where s.user.id = :userId and s.post.id = :postId
            """)
    SavedPost findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

    @Query("""
            select s 
            from SavedPost s 
            where s.user.id = :id
            order by s.savedAt desc 
            """)
    List<SavedPost> findAllByUserId(@Param("id") Long userId);

    @Query("""
            select (count(s) > 0)
            from SavedPost s 
            where s.user.id = :userId and s.post.id = :postId
            """)
    boolean checkSavedByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
}
