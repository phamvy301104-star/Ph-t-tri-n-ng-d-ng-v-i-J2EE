package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByOauthProviderAndOauthId(String provider, String oauthId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
