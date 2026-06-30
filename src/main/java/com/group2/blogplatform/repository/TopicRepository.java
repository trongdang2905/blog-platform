package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    // Hàm 1: CRUD Topic (Dùng Integer)
    @Query("SELECT t FROM Topic t WHERE t.id = :id")
    Topic findByID(@Param("id") Integer id);

    // Hàm 2: PostServiceImpl của Đặng (ép từ Long sang Integer)
    default Topic findByID(Long id) {
        if (id == null) return null;
        return findByID(id.intValue());
    }

    @Query("""
            select t 
            from Topic t 
            """)
    List<Topic> getAllTopics();

    @Query("""
            select t 
            from Topic t 
            where t.isActive = true
            """)
    List<Topic> getAllTopicsWithActive();
}