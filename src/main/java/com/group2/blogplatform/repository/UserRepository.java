package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.Role;
import com.group2.blogplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
                select u
                from User u
                where u.id = :id
            """)
    User findByID(@Param("id") Long id);

    List<User> findByRole(Role role);

    @Query("FROM User u WHERE u.email = :email AND u.passwordHash = :password")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    Boolean existsByEmail(String email);
}
