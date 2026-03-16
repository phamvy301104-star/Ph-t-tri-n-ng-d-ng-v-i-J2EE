package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.service.CourseService;
import com.example.demo.service.EnrollmentService;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentService enrollmentService;

    // trang chu - hien thi danh sach khoa hoc + phan trang
    @GetMapping({ "/", "/home" })
    public String home(@RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model, Authentication authentication) {

        Page<Course> coursePage;
        int pageSize = 5; // hien thi 5 khoa hoc moi trang

        if (search != null && !search.trim().isEmpty()) {
            coursePage = courseService.search(search, page, pageSize);
            model.addAttribute("search", search);
        } else {
            coursePage = courseService.getAll(page, pageSize);
        }

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("totalItems", coursePage.getTotalElements());

        // neu dang dang nhap thi lay danh sach khoa hoc da dang ky
        Long studentId = layStudentId(authentication);
        if (studentId != null) {
            model.addAttribute("studentId", studentId);
            var dsDangKy = enrollmentService.getByStudent(studentId);
            List<Long> enrolledIds = dsDangKy.stream()
                    .map(e -> e.getCourse().getId())
                    .collect(Collectors.toList());
            model.addAttribute("enrolledIds", enrolledIds);
        }

        return "home";
    }

    @GetMapping("/courses")
    public String courses(@RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model, Authentication authentication) {
        return home(page, search, model, authentication);
    }

    // lay studentId tu authentication (ho tro ca form login va oauth2)
    private Long layStudentId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return null;

        Object principal = auth.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) principal;
            Object sid = oauthUser.getAttribute("studentId");
            if (sid != null)
                return Long.valueOf(sid.toString());

            String email = oauthUser.getAttribute("email");
            if (email != null) {
                return studentService.findByEmail(email).map(Student::getStudentId).orElse(null);
            }
        } else if (principal instanceof User) {
            User user = (User) principal;
            return studentService.findByUsername(user.getUsername())
                    .map(Student::getStudentId).orElse(null);
        }
        return null;
    }
}
