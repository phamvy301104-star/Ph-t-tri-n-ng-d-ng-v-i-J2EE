package com.example.demo.controller;

import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public String trangDangNhap(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Sai ten dang nhap hoac mat khau!");
        }
        if (logout != null) {
            model.addAttribute("message", "Ban da dang xuat thanh cong.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String trangDangKy() {
        return "register";
    }

    // xu ly dang ky tai khoan
    @PostMapping("/register")
    public String xuLyDangKy(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            RedirectAttributes redirectAttrs) {
        try {
            studentService.register(username, password, email);
            redirectAttrs.addFlashAttribute("success", "Dang ky thanh cong! Moi ban dang nhap.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
