package com.datn.commonbase.service.implement;

import com.datn.commonbase.constant.ObjectType;
import com.datn.commonbase.entity.Category;
import com.datn.commonbase.repository.CategoryRepository;
import com.datn.commonbase.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    private static final Logger _log = LogManager.getLogger(CategoryServiceImpl.class);

    @Override
    public Map<String, Object> getCategories() {
        Map<String, Object> result = new HashMap<>();
        List<Category> categoriesList = categoryRepository.findCategoriesByParentId(-1);
        if (categoriesList == null || categoriesList.isEmpty()) {
            _log.error("No categories found");
            return null;
        }
        result.put(ObjectType.CATEGORY, categoriesList);

        Map<Long, Object> categoriesMap = new HashMap<>();
        categoriesList.stream().forEach(category -> {
            List<Category> subCategories = categoryRepository.findCategoriesByParentId(category.getCategoryId());
            if (subCategories != null && !subCategories.isEmpty()) {
                categoriesMap.put(category.getCategoryId(), subCategories);
            }
        });
        result.put(ObjectType.SUB_CATEGORY, categoriesMap);
        return result;
    }

    @Override
    public Map<Category, Object> getCategoriesMap() {
        Map<Category, Object> result = new HashMap<>();
        List<Category> categoriesList = categoryRepository.findCategoriesByParentId(-1);
        if (categoriesList == null || categoriesList.isEmpty()) {
            _log.error("No categories found");
            return null;
        }
        categoriesList.stream().forEach(category -> {
            List<Category> subCategories = categoryRepository.findCategoriesByParentId(category.getCategoryId());
            if (subCategories != null && !subCategories.isEmpty()) {
                result.put(category, subCategories);
            }
        });
        return result;
    }

    @Override
    public Category getCategoryById(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return null;
        }
        return category;
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findCategoryByCategoryName(categoryName);
    }

    @Override
    public Category saveCategory(Category category) {
        if (!categoryRepository.existsByCategoryName(category.getCategoryName())) {
            return categoryRepository.save(category);
        }
        return null;
    }

    @Override
    public Category updateCategory(Category category) {
        if (category != null) {
            return categoryRepository.save(category);
        }
        _log.error("Category is null");
        return null;
    }

    @Override
    public void deleteCategory(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<Category> getCategoriesByParentId(long parentId) {
        List<Category> categories = categoryRepository.findCategoriesByParentId(parentId);
        return categories;
    }
}
