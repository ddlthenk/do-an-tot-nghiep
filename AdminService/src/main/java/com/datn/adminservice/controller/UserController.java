package com.datn.adminservice.controller;

import com.datn.commonbase.constant.DefaultValue;
import com.datn.commonbase.constant.ImageTypes;
import com.datn.commonbase.entity.Account;
import com.datn.commonbase.entity.Image;
import com.datn.commonbase.entity.User;
import com.datn.commonbase.entity.Voucher;
import com.datn.commonbase.service.AccountService;
import com.datn.commonbase.service.ImageService;
import com.datn.commonbase.service.UserService;
import com.datn.commonbase.service.VoucherService;
import com.datn.commonbase.util.RoleConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;
    @Autowired
    ImageService imageService;
    @Autowired
    VoucherService voucherService;
    private final Logger _log = LogManager.getLogger(UserController.class);

    @GetMapping("/user-list")
    private String getListUser(Model model) {
        List<User> userList = userService.getUsersByRole(0, 100, RoleConstant.USER.getValue());
        model.addAttribute("userList", userList);
        return "/user/list-user";
    }

    @GetMapping("/user-info/{id}")
    private String getUserInfo(Model model, @PathVariable("id") long id) {
        try {
            User user = userService.getUserById(id);
            Account account = accountService.getAccountById(id);
            model.addAttribute("user", user);
            if (user != null) {
                model.addAttribute("user", user);
                System.out.println(user.getName());
            }
            if (account != null) {
                model.addAttribute("account", account);
            }
            String imageUrl = DefaultValue.DEFAULT_IMAGE_URL;
            List<Image> imageDtos = imageService.getListImage(id, ImageTypes.USER_IMG.getValue());
            if (imageDtos != null && !imageDtos.isEmpty()) {
                imageUrl = imageDtos.get(0).getUrl();
            }
            List<Voucher> voucherList = voucherService.getListVoucherByUserId(id, true, 0, 10);
            model.addAttribute("voucherList", voucherList);
            model.addAttribute("imageUrl", imageUrl);
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return "/user/user-info";
    }

}
