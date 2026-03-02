package com.example.bai4.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int id;
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
}
