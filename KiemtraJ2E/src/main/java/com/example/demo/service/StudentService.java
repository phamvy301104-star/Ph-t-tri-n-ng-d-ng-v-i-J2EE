package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.Student;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // dang ky tai khoan moi
    public Student register(String username, String password, String email) {
        // check trung username va email
        if (studentRepo.existsByUsername(username)) {
            throw new RuntimeException("Username da ton tai!");
        }
        if (studentRepo.existsByEmail(email)) {
            throw new RuntimeException("Email nay da duoc su dung!");
        }

        Student sv = new Student();
        sv.setUsername(username);
        sv.setPassword(passwordEncoder.encode(password));
        sv.setEmail(email);

        // mac dinh la STUDENT
        Role studentRole = roleRepo.findByName("STUDENT")
                .orElseGet(() -> roleRepo.save(new Role("STUDENT")));
        sv.addRole(studentRole);

        return studentRepo.save(sv);
    }

    public Optional<Student> findByUsername(String username) {
        return studentRepo.findByUsername(username);
    }

    public Optional<Student> findByEmail(String email) {
        return studentRepo.findByEmail(email);
    }

    public Optional<Student> findById(Long id) {
        return studentRepo.findById(id);
    }

    // xu ly dang nhap qua google oauth2
    public Student findOrCreateOAuth(String email, String name, String provider, String oauthId) {
        // tim theo oauth truoc
        Optional<Student> existing = studentRepo.findByOauthProviderAndOauthId(provider, oauthId);
        if (existing.isPresent()) {
            return existing.get();
        }

        // tim theo email
        Optional<Student> byEmail = studentRepo.findByEmail(email);
        if (byEmail.isPresent()) {
            Student sv = byEmail.get();
            sv.setOauthProvider(provider);
            sv.setOauthId(oauthId);
            return studentRepo.save(sv);
        }

        // tao moi neu chua co
        Student sv = new Student();
        if (name != null) {
            sv.setUsername(name.replaceAll("\\s+", "_").toLowerCase() + "_" + System.currentTimeMillis());
        } else {
            sv.setUsername(email.split("@")[0]);
        }
        sv.setEmail(email);
        sv.setOauthProvider(provider);
        sv.setOauthId(oauthId);

        Role stuRole = roleRepo.findByName("STUDENT")
                .orElseGet(() -> roleRepo.save(new Role("STUDENT")));
        sv.addRole(stuRole);
        return studentRepo.save(sv);
    }
}
