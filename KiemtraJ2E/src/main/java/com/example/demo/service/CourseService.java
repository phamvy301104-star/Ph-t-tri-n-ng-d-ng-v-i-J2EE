package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Course;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepo;
    private final CategoryRepository categoryRepo;

    // thu muc luu hinh anh upload
    private static final String UPLOAD_DIR = "uploads/images";

    public Page<Course> getAll(int page, int size) {
        return courseRepo.findAll(PageRequest.of(page, size));
    }

    public List<Course> getAll() {
        return courseRepo.findAll();
    }

    public Optional<Course> getById(Long id) {
        return courseRepo.findById(id);
    }

    // tim kiem theo ten khoa hoc
    public Page<Course> search(String keyword, int page, int size) {
        return courseRepo.searchByName(keyword, PageRequest.of(page, size));
    }

    public Course create(Course course, MultipartFile imageFile, Long categoryId) throws IOException {
        if (categoryId != null) {
            course.setCategory(categoryRepo.findById(categoryId).orElse(null));
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = luuHinhAnh(imageFile);
            course.setImage(imagePath);
        }
        return courseRepo.save(course);
    }

    public Course update(Long id, Course data, MultipartFile imageFile, Long categoryId) throws IOException {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay khoa hoc id=" + id));

        course.setName(data.getName());
        course.setCredits(data.getCredits());
        course.setLecturer(data.getLecturer());

        if (categoryId != null) {
            course.setCategory(categoryRepo.findById(categoryId).orElse(null));
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            course.setImage(luuHinhAnh(imageFile));
        }
        return courseRepo.save(course);
    }

    public void delete(Long id) {
        courseRepo.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    // luu file upload vao thu muc uploads/images
    private String luuHinhAnh(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();
        String ext = ".jpg"; // mac dinh
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID().toString() + ext;
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/images/" + fileName;
    }
}
