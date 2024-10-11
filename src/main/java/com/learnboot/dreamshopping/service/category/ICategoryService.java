package com.learnboot.dreamshopping.service.category;

import com.learnboot.dreamshopping.models.Category;

import java.util.List;

public interface ICategoryService {

    Category getCategoryById(long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, long id);
    void deleteCategory(long id);
}
