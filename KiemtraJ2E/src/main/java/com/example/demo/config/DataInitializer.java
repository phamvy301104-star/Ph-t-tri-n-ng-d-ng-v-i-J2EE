package com.example.demo.config;

import com.example.demo.entity.Category;
import com.example.demo.entity.Course;
import com.example.demo.entity.Role;
import com.example.demo.entity.Student;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// class de tao du lieu mau khi khoi dong ung dung
@Configuration
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // tao role mac dinh
            Role adminRole = roleRepo.findByName("ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role("ADMIN")));
            Role studentRole = roleRepo.findByName("STUDENT")
                    .orElseGet(() -> roleRepo.save(new Role("STUDENT")));

            // tao tai khoan admin mac dinh
            if (!studentRepo.existsByUsername("admin")) {
                Student admin = new Student();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@example.com");
                admin.addRole(adminRole);
                studentRepo.save(admin);
            }

            // tao tai khoan student mac dinh
            if (!studentRepo.existsByUsername("student")) {
                Student sv = new Student();
                sv.setUsername("student");
                sv.setPassword(passwordEncoder.encode("student123"));
                sv.setEmail("student@example.com");
                sv.addRole(studentRole);
                studentRepo.save(sv);
            }

            // them du lieu mau neu chua co khoa hoc nao
            if (courseRepo.count() == 0) {
                Category catLapTrinh = categoryRepo.findByName("Programming")
                        .orElseGet(() -> categoryRepo.save(new Category("Programming")));
                Category catCSDL = categoryRepo.findByName("Database")
                        .orElseGet(() -> categoryRepo.save(new Category("Database")));
                Category catWeb = categoryRepo.findByName("Web Development")
                        .orElseGet(() -> categoryRepo.save(new Category("Web Development")));
                Category catAI = categoryRepo.findByName("AI & Machine Learning")
                        .orElseGet(() -> categoryRepo.save(new Category("AI & Machine Learning")));

                taoKhoaHoc("Java Programming", 3, "Dr. Nguyen Van A", catLapTrinh,
                        "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400");
                taoKhoaHoc("Python Basics", 3, "Dr. Tran Thi B", catLapTrinh,
                        "https://images.unsplash.com/photo-1526379095098-d400fd0bf935?w=400");
                taoKhoaHoc("Database Systems", 4, "Dr. Le Van C", catCSDL,
                        "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=400");
                taoKhoaHoc("Spring Boot Web", 4, "Dr. Pham Thi D", catWeb,
                        "https://images.unsplash.com/photo-1627398242454-45a1465c2479?w=400");
                taoKhoaHoc("Machine Learning", 3, "Dr. Hoang Van E", catAI,
                        "https://images.unsplash.com/photo-1555255707-c07966088b7b?w=400");
                taoKhoaHoc("Data Structures", 3, "Dr. Nguyen Thi F", catLapTrinh,
                        "https://images.unsplash.com/photo-1515879218367-8466d910aaa4?w=400");
                taoKhoaHoc("SQL and NoSQL", 3, "Dr. Tran Van G", catCSDL,
                        "https://images.unsplash.com/photo-1489875347897-49f64b51c1f8?w=400");
                taoKhoaHoc("React.js Frontend", 3, "Dr. Le Thi H", catWeb,
                        "https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=400");
                taoKhoaHoc("Deep Learning", 4, "Dr. Pham Van I", catAI,
                        "https://images.unsplash.com/photo-1677442136019-21780ecad995?w=400");
                taoKhoaHoc("Node.js Backend", 3, "Dr. Hoang Thi K", catWeb,
                        "https://images.unsplash.com/photo-1627163439134-7a8c47e08208?w=400");
                taoKhoaHoc("Angular Framework", 3, "Dr. Nguyen Van L", catWeb,
                        "https://images.unsplash.com/photo-1593720219276-0b1eacd0aef4?w=400");
                taoKhoaHoc("Computer Networks", 3, "Dr. Tran Thi M", catLapTrinh,
                        "https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=400");
            }
        };
    }

    // method tao khoa hoc nhanh
    private void taoKhoaHoc(String ten, int tinChi, String giangVien, Category danhMuc, String hinhAnh) {
        Course kh = new Course();
        kh.setName(ten);
        kh.setCredits(tinChi);
        kh.setLecturer(giangVien);
        kh.setCategory(danhMuc);
        kh.setImage(hinhAnh);
        courseRepo.save(kh);
    }
}
