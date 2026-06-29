package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
                select p 
                from Post p 
                order by p.isPinned desc, p.createdAt desc
            """)
    List<Post> getPostByPinnedAndCreated(Pageable pageable);

    @Query("""
                select p 
                from Post p 
                where p.topic.id = :topicId
                order by p.id desc 
            """)
    List<Post> getPostByTopicID(@Param("topicId") Long topicId);

    @Query("""
            select count(p)
            from Post p 
            where p.statusPost = 'PUBLISHED'
            """)
    long countPost();

    @Query("""
            select count(p)
            from Post p 
            where p.statusPost = 'PUBLISHED' and p.topic.id = :topicId
            """)
    long countPostByTopicID(@Param("topicId") Long topicId);


    @Query("""
            select p 
            from Post p 
            where p.id = :id 
            """)
    Post findByID(@Param("id") Long id);

    @Query("""
            SELECT p
            FROM Post p
            WHERE p.title LIKE CONCAT('%', :keyword, '%')
               OR p.content LIKE CONCAT('%', :keyword, '%')
            """)
    List<Post> searchPostByTitleOrContent(@Param("keyword") String keyword);
}
