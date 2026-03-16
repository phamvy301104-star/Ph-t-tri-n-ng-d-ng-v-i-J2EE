package com.example.demo.repository;

import com.example.demo.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // phan trang
    Page<Course> findAll(Pageable pageable);

    // tim kiem theo ten (khong phan biet hoa thuong)
    @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Course> searchByName(@Param("keyword") String keyword, Pageable pageable);

    List<Course> findByCategoryId(Long categoryId);
}
