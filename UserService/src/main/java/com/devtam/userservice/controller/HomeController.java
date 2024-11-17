package com.devtam.userservice.controller;

import com.devtam.commonbase.constant.ImageTypes;
import com.devtam.commonbase.entity.Category;
import com.devtam.commonbase.entity.Image;
import com.devtam.commonbase.entity.Product;
import com.devtam.commonbase.entity.User;
import com.devtam.commonbase.service.*;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    HttpSession session;
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    ImageService imageService;
    @Autowired
    GHNService ghnService;
    private final Logger _log = LogManager.getLogger(HomeController.class);

    @GetMapping({"/home", "/", ""})
    public String home(Authentication authentication, Model model) {
        getCategoryParentList();
        getUserData(authentication);
        /* top selling */
        List<Product> listTopSelling = productService.getTopSelling(true, 0, 6).getContent();
        model.addAttribute("listTopSelling", listTopSelling);
        List<Long> topSellingIds = listTopSelling.stream().map(Product::getProductId).collect(Collectors.toList());
        Map<Long, Image> topSellingMap = imageService.getMapOneImage(topSellingIds, ImageTypes.PRODUCT_IMG.getValue());
        model.addAttribute("topSellingMap", topSellingMap);
        /* newest product */
        List<Product> listNewProduct = productService.getNewestProductsList(true, 0, 6).getContent();
        model.addAttribute("listNewProduct", listNewProduct);
        List<Long> newestProductIds = listNewProduct.stream().map(Product::getProductId).collect(Collectors.toList());
        Map<Long, Image> newestProductImMap = imageService.getMapOneImage(newestProductIds, ImageTypes.PRODUCT_IMG.getValue());
        model.addAttribute("newestProductImMap", newestProductImMap);
        return "/home";
    }

    @GetMapping({"/about"})
    public String about(Authentication authentication, Model model) {
        getCategoryParentList();
        getUserData(authentication);
        return "/about";
    }

    @GetMapping({"/error"})
    public String error(Authentication authentication, Model model) {
        getCategoryParentList();
        getUserData(authentication);
        return "/error";
    }

    private void getCategoryParentList() {
        List<Category> categoryList = categoryService.getCategoriesByParentId(-1);
        if (session.getAttribute("categoryParents") == null) {
            if (categoryList != null && !categoryList.isEmpty()) {
                session.setAttribute("categoryParents", categoryList);
            }
        }
        if (session.getAttribute("imageMap") == null) {
            if (categoryList != null && !categoryList.isEmpty()) {
                List<Long> categoryIds = categoryList.stream().map(Category::getCategoryId).collect(Collectors.toList());
                Map<Long, Image> imageMap = imageService.getMapOneImage(categoryIds, ImageTypes.CATEGORY_IMG.getValue());
                if (imageMap != null && !imageMap.isEmpty()) {
                    session.setAttribute("imageMap", imageMap);
                }
            }
        }
    }

    private void getUserData(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername(); // Lấy userId từ UserDetails
            if (session.getAttribute("user") == null) {
                User user = userService.getUserById(Long.valueOf(userId));
                if (user.getName() == null) {
                    user.setName("");
                }
                session.setAttribute("user", user);
            }
            if (session.getAttribute("image") == null) {
                List<Image> imageList = imageService.getListImage(Long.valueOf(userId), ImageTypes.USER_IMG.getValue());
                if (imageList != null && !imageList.isEmpty()) {
                    session.setAttribute("image", imageList.get(0));
                }
            }

        } catch (Exception e) {
            _log.error(e.getMessage());
        }
    }

}
