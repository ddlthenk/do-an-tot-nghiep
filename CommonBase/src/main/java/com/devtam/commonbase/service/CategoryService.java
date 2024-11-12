package com.devtam.commonbase.service;


import com.devtam.commonbase.entity.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    public Map<String, Object> getCategories();

    public Map<Category, Object> getCategoriesMap();

    public Category getCategoryById(long categoryId);

    public Category getCategoryByName(String categoryName);

    public Category saveCategory(Category category);

    public Category updateCategory(Category category);

    public void deleteCategory(long categoryId);

    public List<Category> getCategoriesByParentId(long parentId);
}
