package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private CourseRepository courseRepo;

    // dang ky hoc phan cho sinh vien
    public Enrollment enroll(Long studentId, Long courseId) {
        // kiem tra da dang ky chua
        if (enrollmentRepo.existsByStudentStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Ban da dang ky khoa hoc nay roi!");
        }

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay sinh vien"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay khoa hoc"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollmentRepo.save(enrollment);
    }

    public List<Enrollment> getByStudent(Long studentId) {
        return enrollmentRepo.findByStudentStudentId(studentId);
    }

    public boolean isEnrolled(Long studentId, Long courseId) {
        return enrollmentRepo.existsByStudentStudentIdAndCourseId(studentId, courseId);
    }

    // huy dang ky hoc phan
    public void unenroll(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepo
                .findByStudentStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Chua dang ky khoa hoc nay"));
        enrollmentRepo.delete(enrollment);
    }
}
