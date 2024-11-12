package com.devtam.commonbase.repository;


import com.devtam.commonbase.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findByParentId(long parentId);

    public List<Category> findCategoriesByParentId(long parentId);

    public boolean existsByCategoryName(String categoryName);

    public Category findCategoryByCategoryName(String categoryName);
}
