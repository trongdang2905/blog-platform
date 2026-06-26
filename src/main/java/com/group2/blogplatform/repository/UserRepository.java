package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("""
                select u
                from User u
                where u.id = :id
            """)
    User findByID(@Param("id") Long id);
}
