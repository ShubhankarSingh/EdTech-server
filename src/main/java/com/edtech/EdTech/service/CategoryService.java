package com.edtech.EdTech.service;

import com.edtech.EdTech.model.courses.Category;

import java.util.List;

public interface CategoryService {

    int findCategoryId(Category category);

    List<Category> getAllCategories();

}
