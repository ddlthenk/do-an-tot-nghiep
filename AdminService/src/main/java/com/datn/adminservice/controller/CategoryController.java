package com.datn.adminservice.controller;

import com.datn.commonbase.entity.Category;
import com.datn.commonbase.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/get-category-children/{parentId}")
    @ResponseBody
    public List<Category> getCategoryChildren(@PathVariable("parentId") long parentId) {
        List<Category> categories = categoryService.getCategoriesByParentId(parentId);
        return categories;
    }
}
