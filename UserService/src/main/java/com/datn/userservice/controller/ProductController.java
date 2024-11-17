package com.datn.userservice.controller;

import com.datn.commonbase.constant.ImageTypes;
import com.datn.commonbase.entity.*;
import com.datn.commonbase.service.*;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ProductController {
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
    CommentService commentService;
    private final Logger _log = LogManager.getLogger(ProductController.class);

    @GetMapping({"/products/category/{categoryId}"})
    public String productsByCategory(Authentication authentication, Model model,
                                     @PathVariable("categoryId") long categoryId,
                                     @RequestParam(value = "sort", defaultValue = "") String sort,
                                     @RequestParam(value = "from", defaultValue = "-1") long from,
                                     @RequestParam(value = "to", defaultValue = "-1") long to,
                                     @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        if (page > 0) {
            page--;
        }
        getCategoryParentList();
        getUserData(authentication);
        /* category */
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        /* product list */
        Page<Product> pageProductByCategory = productService.getSortedProductPageByCategory(categoryId, sort, from, to, true, page, 8);

        if (page > pageProductByCategory.getTotalPages()) {
            return "redirect:/products/category/" + categoryId;
        }
        model.addAttribute("pageProductByCategory", pageProductByCategory);
        List<Long> topSellingIds = pageProductByCategory.getContent().stream().map(Product::getProductId).collect(Collectors.toList());
        Map<Long, Image> productImageMap = imageService.getMapOneImage(topSellingIds, ImageTypes.PRODUCT_IMG.getValue());
        model.addAttribute("productImageMap", productImageMap);
        return "product/product-list-cate";
    }

    @GetMapping({"/products/{listType}"})
    public String productsByCategory(Authentication authentication, Model model, @PathVariable("listType") String listType
            , @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        if (page > 0) {
            page--;
        }
        getCategoryParentList();
        getUserData(authentication);
        /* product list */
        Page<Product> pageProduct;
        if (listType.equalsIgnoreCase("san-pham-ban-chay")) {
            model.addAttribute("pageType", "san-pham-ban-chay");
            model.addAttribute("pageName", "Sản phẩm bán chạy");
            pageProduct = productService.getTopSelling(true, page, 8);
            if (page > pageProduct.getTotalPages()) {
                return "redirect:/products/san-pham-ban-chay";
            }
            model.addAttribute("pageProduct", pageProduct);
            List<Long> productIds = pageProduct.getContent().stream().map(Product::getProductId).collect(Collectors.toList());
            if (!productIds.isEmpty()) {
                Map<Long, Image> productImageMap = imageService.getMapOneImage(productIds, ImageTypes.PRODUCT_IMG.getValue());
                model.addAttribute("productImageMap", productImageMap);
            }
        }
        if (listType.equalsIgnoreCase("san-pham-moi")) {
            model.addAttribute("pageType", "san-pham-moi");
            model.addAttribute("pageName", "Sản phẩm mới");
            pageProduct = productService.getNewestProductsList(true, page, 8);
            if (page > pageProduct.getTotalPages()) {
                return "redirect:/products/san-pham-ban-chay";
            }
            model.addAttribute("pageProduct", pageProduct);
            List<Long> productIds = pageProduct.getContent().stream().map(Product::getProductId).collect(Collectors.toList());
            if (!productIds.isEmpty()) {
                Map<Long, Image> productImageMap = imageService.getMapOneImage(productIds, ImageTypes.PRODUCT_IMG.getValue());
                model.addAttribute("productImageMap", productImageMap);
            }
        }
        return "product/product-list";
    }

    @GetMapping("/search")
    public String search(Authentication authentication, Model model,
                         @RequestParam(required = false, value = "searchValue", defaultValue = "") String searchValue,
                         @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        if (page > 0) {
            page--;
        }
        getCategoryParentList();
        getUserData(authentication);
        /* product list */
        Page<Product> pageProduct;
        model.addAttribute("pageName", "Tìm kiếm");
        pageProduct = productService.searchProducts(true, page, 8, searchValue);
        if (page > pageProduct.getTotalPages()) {
            return "redirect:/error";
        }
        model.addAttribute("pageProduct", pageProduct);
        List<Long> productIds = pageProduct.getContent().stream().map(Product::getProductId).collect(Collectors.toList());
        if (!productIds.isEmpty()) {
            Map<Long, Image> productImageMap = imageService.getMapOneImage(productIds, ImageTypes.PRODUCT_IMG.getValue());
            model.addAttribute("productImageMap", productImageMap);
        }
        return "product/product-search";
    }

    @GetMapping("product-details/{productId}")
    public String productDetails(Model model, Authentication authentication, @PathVariable("productId") long productId) {
        getCategoryParentList();
        getUserData(authentication);
        Product product = productService.getProduct(productId);
        if (product == null) {
            return "/404";
        }
        List<Comment> commentList = commentService.getCommentsOfProduct(productId, 0, 8);
        List<Long> userIds = commentList.stream().map(Comment::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap = userService.getMapUsers(userIds);

        model.addAttribute("commentList", commentList);
        model.addAttribute("userMap", userMap);
        model.addAttribute("product", product);
        List<Image> imageList = imageService.getListImage(productId, ImageTypes.PRODUCT_IMG.getValue());
        model.addAttribute("imageList", imageList);
        return "product/product-details";
    }

    private void getCategoryParentList() {
        List<Category> categoryList = new ArrayList<>();
        if (session.getAttribute("categoryParents") == null) {
            categoryList = categoryService.getCategoriesByParentId(-1);
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
