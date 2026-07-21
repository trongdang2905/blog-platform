package com.group2.blogplatform.repository;

import com.group2.blogplatform.entity.Role;
import com.group2.blogplatform.entity.StatusUser;
import com.group2.blogplatform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // --- BỔ SUNG CHO PHÂN TRANG (PAGINATION) & FILTER ---

    // 1. Phân trang tất cả User
    Page<User> findAll(Pageable pageable);

    // 2. Tìm kiếm theo Username hoặc Email có phân trang
    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String username, String email, Pageable pageable);

    // 3. Lọc theo Trạng thái (ACTIVE / BANNED) có phân trang
    Page<User> findByStatusUser(StatusUser statusUser, Pageable pageable);

    // 4. Kết hợp cả Tìm kiếm và Lọc theo Trạng thái có phân trang
    Page<User> findByStatusUserAndUsernameContainingIgnoreCaseOrStatusUserAndEmailContainingIgnoreCase(
            StatusUser status1, String username, StatusUser status2, String email, Pageable pageable);
}