package com.example.demo.controller;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Student;
import com.example.demo.service.EnrollmentService;
import com.example.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    @PostMapping("/enroll/{courseId}")
    public String dangKyHocPhan(@PathVariable Long courseId,
            Authentication auth, RedirectAttributes redirectAttrs) {
        try {
            Long studentId = layStudentId(auth);
            if (studentId == null) {
                redirectAttrs.addFlashAttribute("error", "Khong xac dinh duoc sinh vien");
                return "redirect:/home";
            }
            enrollmentService.enroll(studentId, courseId);
            redirectAttrs.addFlashAttribute("success", "Dang ky hoc phan thanh cong!");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/my-courses")
    public String xemHocPhanCuaToi(Authentication auth, Model model) {
        Long studentId = layStudentId(auth);
        if (studentId == null) {
            return "redirect:/login";
        }
        List<Enrollment> danhSach = enrollmentService.getByStudent(studentId);
        model.addAttribute("enrollments", danhSach);
        return "my-courses";
    }

    @PostMapping("/unenroll/{courseId}")
    public String huyDangKy(@PathVariable Long courseId,
            Authentication auth, RedirectAttributes redirectAttrs) {
        try {
            Long studentId = layStudentId(auth);
            if (studentId == null) {
                redirectAttrs.addFlashAttribute("error", "Khong xac dinh duoc sinh vien");
                return "redirect:/my-courses";
            }
            enrollmentService.unenroll(studentId, courseId);
            redirectAttrs.addFlashAttribute("success", "Da huy dang ky hoc phan!");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/my-courses";
    }

    // helper: lay student id tu authentication
    private Long layStudentId(Authentication auth) {
        if (auth == null)
            return null;
        Object principal = auth.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User ou = (OAuth2User) principal;
            Object sid = ou.getAttribute("studentId");
            if (sid != null) {
                return Long.valueOf(sid.toString());
            }
            String email = ou.getAttribute("email");
            if (email != null) {
                return studentService.findByEmail(email)
                        .map(Student::getStudentId).orElse(null);
            }
        } else if (principal instanceof User) {
            String username = ((User) principal).getUsername();
            return studentService.findByUsername(username)
                    .map(Student::getStudentId).orElse(null);
        }
        return null;
    }
}
