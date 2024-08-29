package com.edtech.EdTech.repository;

import com.edtech.EdTech.model.courses.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    Category findByCategoryType(String category);
}
