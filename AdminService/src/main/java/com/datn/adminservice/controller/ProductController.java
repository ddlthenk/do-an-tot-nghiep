package com.datn.adminservice.controller;

import com.datn.commonbase.constant.ImageTypes;
import com.datn.commonbase.entity.*;
import com.datn.commonbase.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductDetailsService productDetailsService;
    CloudinaryService cloudinaryService = new CloudinaryService();
    private final Logger _log = LogManager.getLogger(ProductController.class);

    @GetMapping({"/products", "/products/"})
    public String getListProducts(Model model, @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        try {
            if (page > 0) {
                page--;
            }
            List<Category> categoryList = categoryService.getCategoriesByParentId(-1);
            model.addAttribute("categories", categoryList);
            Page<Product> productPage = productService.getPageProducts(-1, page, 8, true);
            List<Long> productIds = productPage.getContent().stream().map(Product::getProductId).collect(Collectors.toList());
            Map<Long, Image> imageMap = imageService.getMapOneImage(productIds, ImageTypes.PRODUCT_IMG.getValue());
            model.addAttribute("imageMap", imageMap);
            model.addAttribute("productPage", productPage);
            return "product/product-list";
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "redirect:/admin/error";
        }
    }

    @GetMapping({"/products/{categoryId}"})
    public String getListProductsBycategory(Model model, @RequestParam(required = false, value = "page", defaultValue = "0") int page, @PathVariable("categoryId") long categoryId) {
        try {
            if (page > 0) {
                page--;
            }
            List<Category> categoryList = categoryService.getCategoriesByParentId(-1);
            model.addAttribute("categories", categoryList);

            Category category = categoryService.getCategoryById(categoryId);
            model.addAttribute("category", category);

            Page<Product> productPage = productService.getPageProducts(categoryId, page, 12, true);
            model.addAttribute("productPage", productPage);
            List<Long> productIds = productPage.getContent().stream().map(Product::getProductId).collect(Collectors.toList());
            Map<Long, Image> imageMap = imageService.getMapOneImage(productIds, ImageTypes.PRODUCT_IMG.getValue());
            model.addAttribute("imageMap", imageMap);
            model.addAttribute("categoryId", categoryId);
            return "product/product-list-cate";
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "redirect:/admin/error";
        }
    }

    @PostMapping("/products/search")
    public String searchProducts(Model model, @RequestParam(required = false, value = "page", defaultValue = "0") int page
            , @RequestParam("searchValue") String searchValue) {
        try {
            if (page > 0) {
                page--;
            }
            List<Category> categoryList = categoryService.getCategoriesByParentId(-1);
            model.addAttribute("categories", categoryList);
            Page<Product> productPage = productService.searchProducts(true, page, 10, searchValue);
            List<Long> productIds = productPage.getContent().stream().map(Product::getProductId).collect(Collectors.toList());
            Map<Long, Image> imageMap = imageService.getMapOneImage(productIds, ImageTypes.PRODUCT_IMG.getValue());
            model.addAttribute("imageMap", imageMap);
            model.addAttribute("productPage", productPage);
            return "product/product-list";
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "redirect:/admin/error";
        }
    }

    @GetMapping("/upload-product")
    public String addProduct(Model model) {
        List<Category> categoryList = categoryService.getCategoriesByParentId(-1);
        model.addAttribute("categoryList", categoryList);

        Product product = new Product();
        product.setProductDetailsList(new ArrayList<>());
        product.getProductDetailsList().add(ProductDetails.builder().optionValue("").build());
        List<ProductDetails> productDetailsList = product.getProductDetailsList();

        model.addAttribute("product", product);
        model.addAttribute("productDetailsList2", productDetailsList);

        _log.error("log");
        return "product/add-product";
    }

    @PostMapping("/upload-product")
    public String postProduct(@ModelAttribute Product product, BindingResult result, @ModelAttribute("categoryChildSelect") long categoryChildSelect
            , @RequestParam("image_upload") MultipartFile[] files
    ) {
        product.setCategoryId(categoryChildSelect);
        Category category = categoryService.getCategoryById(categoryChildSelect);
        product.setCategoryParentId(category.getParentId());
        Product saved = productService.saveProduct(product);
        if (files != null) {
            for (MultipartFile file : files) {
                String urlImg = cloudinaryService.uploadFile(file);
                Image image = Image.builder().imageType(ImageTypes.PRODUCT_IMG.getValue()).url(urlImg)
                        .referenceId(product.getProductId()).build();
                Image savedImage = imageService.saveImage(image);
            }
        }
        return "redirect:/admin/upload-product?uploaded=true";
    }

    @GetMapping("/update-product/{id}")
    public String update(Model model, @PathVariable("id") long productId) {
        List<Category> categoryList = categoryService.getCategoriesByParentId(-1);
        model.addAttribute("categoryList", categoryList);

        Product product = productService.getProduct(productId);
        List<ProductDetails> productDetailsList = product.getProductDetailsList();

        Category category = categoryService.getCategoryById(product.getCategoryId());
        List<Category> categoryChilds = categoryService.getCategoriesByParentId(category.getParentId());

        List<Image> imageList = imageService.getListImage(productId, ImageTypes.PRODUCT_IMG.getValue());
        model.addAttribute("imageList", imageList);
        model.addAttribute("product", product);
        model.addAttribute("productDetailsList2", productDetailsList);
        model.addAttribute("categoryChilds", categoryChilds);
        model.addAttribute("category", category);
        _log.error("log");
        return "product/update-product";
    }

    @PostMapping("/update-product")
    public String updateProduct(@ModelAttribute Product product, BindingResult result
            , @RequestParam("image_upload") MultipartFile[] multipartFile, @ModelAttribute("delete-image") String deleteImages
    ) {
        String[] idTrans = deleteImages.split(",");
        if (idTrans.length > 0 && !Objects.equals(idTrans[0], "")) {
            long[] imageDeletedIds = new long[idTrans.length];
            for (int i = 0; i < imageDeletedIds.length; i++) {
                imageDeletedIds[i] = Long.parseLong(idTrans[i]);
                Image imageDele = imageService.getImage(imageDeletedIds[i]);
                cloudinaryService.deleteFile(imageDele.getUrl());
                imageService.deleteImage(imageDele);
            }
        }
        product.setProductUpdated(System.currentTimeMillis());
        for (ProductDetails productDetails : product.getProductDetailsList()) {
            ProductDetails oldPd = productDetailsService.getProductDetails(productDetails.getDetailId());
            if (productDetails.getTotal() != oldPd.getTotal()) {
                int sold = oldPd.getTotal() - oldPd.getLeftQuantity();
                productDetails.setLeftQuantity(productDetails.getTotal() - sold);
            }
        }
        product.countProductTotal();
        Product saved = productService.saveProduct(product);

        if (multipartFile != null && multipartFile.length > 0 && !Objects.equals(multipartFile[0].getOriginalFilename(), "")) {
            for (MultipartFile file : multipartFile) {
                String urlImg = cloudinaryService.uploadFile(file);
                Image image = Image.builder().imageType(ImageTypes.PRODUCT_IMG.getValue()).url(urlImg)
                        .referenceId(product.getProductId()).build();
                Image savedImage = imageService.saveImage(image);
            }
        }
        return "redirect:/admin/update-product/" + product.getProductId() + "?uploaded=true";
    }

    @GetMapping("/product-details/{productId}")
    public String productDetails(Model model, @PathVariable("productId") long productId) {
        Product product = productService.getProduct(productId);
        model.addAttribute("product", product);
        List<Image> imageList = imageService.getListImage(productId, ImageTypes.PRODUCT_IMG.getValue());
        model.addAttribute("imageList", imageList);
        Category category = categoryService.getCategoryById(product.getCategoryId());
        model.addAttribute("category", category);
        Category categoryParent = categoryService.getCategoryById(category.getParentId());
        model.addAttribute("categoryParent", categoryParent);
        List<Comment> commentList = commentService.getCommentsOfProduct(productId, 0, 8);
        List<Long> userIds = commentList.stream().map(Comment::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap = userService.getMapUsers(userIds);
        model.addAttribute("userMap", userMap);
        model.addAttribute("commentList", commentList);
        return "product/product-details";
    }

    @GetMapping("/deactive/{productId}")
    public String deactive(@PathVariable("productId") long productId, HttpServletRequest httpServletRequest) {
        Product product = productService.getProduct(productId);
        if (product.isProductStatus()) {
            product.setProductStatus(false);
        } else {
            product.setProductStatus(true);
        }
        productService.saveProduct(product);

        String returnUrl = httpServletRequest.getHeader("Referer");
        if (returnUrl != null) {
            return "redirect:" + returnUrl;
        }
        return "redirect:/admin/product-details/" + productId;
    }
}
