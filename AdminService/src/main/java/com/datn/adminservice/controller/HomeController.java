package com.datn.adminservice.controller;

import com.datn.commonbase.constant.ImageTypes;
import com.datn.commonbase.entity.Image;
import com.datn.commonbase.entity.Order;
import com.datn.commonbase.entity.Product;
import com.datn.commonbase.entity.User;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class HomeController {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    HttpSession session;
    @Autowired
    ProductService productService;
    @Autowired
    ImageService imageService;
    private final Logger _log = LogManager.getLogger(HomeController.class);

    @GetMapping({"/", "/home", ""})
    public String home(Model model, Authentication authentication) {
        model.addAttribute("image_url", "/img/profile-pic.png");
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername(); // Lấy userId từ UserDetails
            User user = userService.getUserById(Long.valueOf(userId));
            if (user == null) {
                return "redirect:/admin/login?login-error=1";
            }
            if (user.getName() == null) {
                user.setName("");
            }
            session.setAttribute("user", user);
            String imageUrl = "/img/profile-pic.png";
            List<Image> imageDtos = imageService.getListImage(Long.parseLong(userId), ImageTypes.USER_IMG.getValue());
            if (imageDtos != null && !imageDtos.isEmpty()) {
                imageUrl = imageDtos.get(0).getUrl();
            }
            session.setAttribute("imageUrl", imageUrl);
            Page<Order> orderPage = orderService.getOrderPage(0, 10);
            model.addAttribute("orderPage", orderPage);

            List<Long> totalSales = orderService.getLast12MonthTotalSales();
            model.addAttribute("totalSales", totalSales);

            List<Product> listTopSelling = productService.getTopSelling(true, 0, 7).getContent();
            model.addAttribute("listTopSelling", listTopSelling);
            List<Long> topSellingIds = listTopSelling.stream().map(Product::getProductId).collect(Collectors.toList());
            Map<Long, Image> topSellingMap = imageService.getMapOneImage(topSellingIds, ImageTypes.PRODUCT_IMG.getValue());
            model.addAttribute("topSellingMap", topSellingMap);

            Long activeUser = accountService.countActiveUser();
            Long allUser = accountService.countAlluser();
            model.addAttribute("activeUser", activeUser);
            model.addAttribute("allUser", allUser);

            Long totalOrder = orderService.getTotalOrder();
            Long totalDoneOrder = orderService.getTotalDoneOrder();
            model.addAttribute("totalOrder", totalOrder);
            model.addAttribute("totalDoneOrder", totalDoneOrder);

            Long totalPrice = orderService.getTotalPriceOrder();
            totalPrice = totalPrice == null ? 0 : totalPrice;
            List<Long> listTotalPrice = orderService.getListPriceOrder();
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("listTotalPrice", listTotalPrice);

            Long totalProfit = orderService.getTotalProfits();
            totalProfit = totalProfit == null ? 0 : totalProfit;
            List<Long> listTotalProfit = orderService.getListProfits();
            model.addAttribute("totalProfit", totalProfit);
            model.addAttribute("listTotalProfits", listTotalProfit);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(value = "login-error", defaultValue = "") String error) {
        model.addAttribute("error", error);
        return "login";
    }
}
