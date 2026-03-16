package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // trang dashboard admin
    @GetMapping
    public String dashboard(Model model) {
        List<Course> tatCaKhoaHoc = courseService.getAll();

        model.addAttribute("courseCount", tatCaKhoaHoc.size());
        model.addAttribute("studentCount", studentRepository.count());
        model.addAttribute("categoryCount", courseService.getAllCategories().size());
        model.addAttribute("enrollmentCount", enrollmentRepository.count());

        // hien thi 5 khoa hoc gan nhat
        int size = tatCaKhoaHoc.size();
        List<Course> recent = size > 5 ? tatCaKhoaHoc.subList(size - 5, size) : tatCaKhoaHoc;
        model.addAttribute("recentCourses", recent);

        return "admin/dashboard";
    }

    // danh sach khoa hoc
    @GetMapping("/courses")
    public String danhSachKhoaHoc(Model model) {
        model.addAttribute("courses", courseService.getAll());
        return "admin/courses";
    }

    @GetMapping("/courses/create")
    public String formTaoKhoaHoc(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", courseService.getAllCategories());
        return "admin/course-form";
    }

    @PostMapping("/courses/create")
    public String xuLyTaoKhoaHoc(@ModelAttribute Course course,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) Long categoryId,
            RedirectAttributes redirectAttrs) {
        try {
            courseService.create(course, imageFile, categoryId);
            redirectAttrs.addFlashAttribute("success", "Them khoa hoc thanh cong!");
        } catch (IOException e) {
            redirectAttrs.addFlashAttribute("error", "Loi khi tao: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String formSuaKhoaHoc(@PathVariable Long id, Model model) {
        Course course = courseService.getById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay khoa hoc voi id=" + id));
        model.addAttribute("course", course);
        model.addAttribute("categories", courseService.getAllCategories());
        return "admin/course-form";
    }

    @PostMapping("/courses/edit/{id}")
    public String xuLySuaKhoaHoc(@PathVariable Long id,
            @ModelAttribute Course course,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) Long categoryId,
            RedirectAttributes redirectAttrs) {
        try {
            courseService.update(id, course, imageFile, categoryId);
            redirectAttrs.addFlashAttribute("success", "Cap nhat khoa hoc thanh cong!");
        } catch (IOException e) {
            redirectAttrs.addFlashAttribute("error", "Loi: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/delete/{id}")
    public String xoaKhoaHoc(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            courseService.delete(id);
            redirectAttrs.addFlashAttribute("success", "Xoa khoa hoc thanh cong!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Khong the xoa: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }
}
