package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("""
            select t 
            from Topic t 
            where t.id = :id
            """)
    Topic findByID(@Param("id") Long id);
}
