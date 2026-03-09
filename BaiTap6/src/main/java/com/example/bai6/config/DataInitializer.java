package com.example.bai6.config;

import com.example.bai6.model.Account;
import com.example.bai6.model.Category;
import com.example.bai6.model.Product;
import com.example.bai6.model.Role;
import com.example.bai6.repository.AccountRepository;
import com.example.bai6.repository.CategoryRepository;
import com.example.bai6.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        // Check if accounts already have correct passwords by checking if any account
        // exists
        List<Account> accounts = accountRepository.findAll();

        // Get roles from DB
        List<Role> roles = entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        Role adminRole = roles.stream().filter(r -> r.getName().equals("ROLE_ADMIN")).findFirst().orElse(null);
        Role userRole = roles.stream().filter(r -> r.getName().equals("ROLE_USER")).findFirst().orElse(null);

        if (adminRole == null || userRole == null) {
            return;
        }

        // Update admin account
        Account admin = accountRepository.findByLoginName("admin").orElse(null);
        if (admin != null) {
            admin.setPassword(passwordEncoder.encode("admin123"));
            accountRepository.save(admin);
            System.out.println("Updated admin password hash");
        }

        // Update user1 account
        Account user1 = accountRepository.findByLoginName("user1").orElse(null);
        if (user1 != null) {
            user1.setPassword(passwordEncoder.encode("user123"));
            accountRepository.save(user1);
            System.out.println("Updated user1 password hash");
        }

        // Seed sample products if empty
        if (productRepository.count() == 0) {
            List<Category> categories = categoryRepository.findAll();
            if (!categories.isEmpty()) {
                Category cat1 = categories.get(0);
                Category cat2 = categories.size() > 1 ? categories.get(1) : cat1;
                Category cat3 = categories.size() > 2 ? categories.get(2) : cat1;

                Product p1 = new Product();
                p1.setName("iPhone 15 Pro");
                p1.setPrice(999999L);
                p1.setImage(
                        "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-pro-finish-select-202309-6-1inch_GEO_US?wid=300");
                p1.setCategory(cat1);
                productRepository.save(p1);

                Product p2 = new Product();
                p2.setName("Samsung Galaxy S24");
                p2.setPrice(799999L);
                p2.setImage(
                        "https://images.samsung.com/vn/smartphones/galaxy-s24/images/galaxy-s24-highlights-color-bg.jpg");
                p2.setCategory(cat1);
                productRepository.save(p2);

                Product p3 = new Product();
                p3.setName("MacBook Air M3");
                p3.setPrice(1299000L);
                p3.setImage(
                        "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/mba13-midnight-select-202402?wid=300");
                p3.setCategory(cat2);
                productRepository.save(p3);

                Product p4 = new Product();
                p4.setName("iPad Air");
                p4.setPrice(699000L);
                p4.setImage(
                        "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/ipad-air-select-wifi-blue-202203?wid=300");
                p4.setCategory(cat3);
                productRepository.save(p4);

                System.out.println("Seeded 4 sample products");
            }
        }
    }
}
