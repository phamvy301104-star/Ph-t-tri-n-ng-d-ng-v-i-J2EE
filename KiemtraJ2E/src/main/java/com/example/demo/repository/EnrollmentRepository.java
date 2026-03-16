package com.example.demo.repository;

import com.example.demo.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentStudentId(Long studentId);

    Optional<Enrollment> findByStudentStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentStudentIdAndCourseId(Long studentId, Long courseId);
}
