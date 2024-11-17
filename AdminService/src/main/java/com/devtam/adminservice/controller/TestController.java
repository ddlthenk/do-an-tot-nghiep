package com.devtam.adminservice.controller;

import com.devtam.commonbase.constant.ImageTypes;
import com.devtam.commonbase.entity.Image;
import com.devtam.commonbase.entity.Product;
import com.devtam.commonbase.entity.ProductDetails;
import com.devtam.commonbase.service.CloudinaryService;
import com.devtam.commonbase.service.ImageService;
import com.devtam.commonbase.service.ProductService;
import com.devtam.commonbase.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

//@Controller
public class TestController {
    //    @Autowired
    ProductService productService;
    CloudinaryService cloudinaryService = new CloudinaryService();
    //    @Autowired
    ImageService imageService;
    //    @Autowired
    UserService userService;


    @GetMapping("/login")
    public String login() {
        System.err.println("Login page");
        return "login";
    }

    @GetMapping("/index")
    public String index() {
        System.err.println("Index page");
        return "index";
    }

    @GetMapping("/logout1")
    public String logout() {
        System.err.println("Logout page");
        return "logout";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        Product product = new Product();
        product.setProductDetailsList(new ArrayList<>());
        product.getProductDetailsList().add(ProductDetails.builder().optionValue("hehe").build());
        model.addAttribute("product", product);
        List<ProductDetails> productDetails = product.getProductDetailsList();
        model.addAttribute("productDetails", productDetails);
        return "home";
    }

    @GetMapping("/testPD/{productId}")
    public String testPD(Model model, @PathVariable("productId") long productId) {
        Product product = productService.getProduct(productId);
        model.addAttribute("product", product);
        List<Image> imageList = imageService.getListImage(productId, ImageTypes.PRODUCT_IMG.getValue());
        model.addAttribute("imageList", imageList);
        return "productDetail";
    }

    @PostMapping("/home")
    public String postHome(@ModelAttribute("editor1") String editor, HttpSession session) {
        System.out.println(editor);
        session.setAttribute("content", editor);
        return "home";
    }

}
