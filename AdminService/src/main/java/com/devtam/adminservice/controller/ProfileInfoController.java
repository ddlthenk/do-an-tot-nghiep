package com.devtam.adminservice.controller;

import com.devtam.commonbase.constant.DefaultValue;
import com.devtam.commonbase.constant.ImageTypes;
import com.devtam.commonbase.entity.Account;
import com.devtam.commonbase.entity.Image;
import com.devtam.commonbase.entity.User;
import com.devtam.commonbase.service.AccountService;
import com.devtam.commonbase.service.CloudinaryService;
import com.devtam.commonbase.service.ImageService;
import com.devtam.commonbase.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class ProfileInfoController {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;
    private CloudinaryService cloudinaryService = new CloudinaryService();
    private final Logger _log = LogManager.getLogger(ProfileInfoController.class);

    @GetMapping("/profile-info")
    public String profileInfo(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            long userId = Long.parseLong(userDetails.getUsername());
            User user = userService.getUserById(userId);
            Account account = accountService.getAccountById(userId);
            if (user != null) {
                model.addAttribute("user", user);
                System.out.println(user.getName());
            }
            if (account != null) {
                model.addAttribute("account", account);
            }
            String imageUrl = DefaultValue.DEFAULT_IMAGE_URL;
            List<Image> imageDtos = imageService.getListImage(userId, ImageTypes.USER_IMG.getValue());
            if (imageDtos != null && !imageDtos.isEmpty()) {
                imageUrl = imageDtos.get(0).getUrl();
            }
            model.addAttribute("imageUrl", imageUrl);
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return "profile-info";
    }

    @PostMapping("/profile-info")
    public String updateProfileInfo(Authentication authentication
            , @ModelAttribute("fullname") String fullname
            , @ModelAttribute("username") String username
            , @ModelAttribute("email") String email
            , @ModelAttribute("phoneNumber") String phoneNumber
            , @ModelAttribute("dateOfBirth") Date dateOfBirth
            , @RequestParam(value = "imageUpload", required = false) MultipartFile[] multipartFile
    ) {
        try {
            long customerId = Long.parseLong(authentication.getName());
            /* update image */
            User user = User.builder().userId(customerId).name(fullname).dateOfBirth(dateOfBirth).build();
            userService.updateUser(customerId, user);
            Account account = Account.builder().accountId(customerId).userName(username).email(email).phoneNumber(phoneNumber).build();
            accountService.updateAccount(customerId, account);
            /* handle image */
            if (multipartFile != null && multipartFile.length > 0 && !Objects.equals(multipartFile[0].getOriginalFilename(), "")) {
                List<Image> imageList = imageService.getListImage(customerId, ImageTypes.USER_IMG.getValue());
                if (imageList == null || imageList.isEmpty()) {
                    String urlImg = cloudinaryService.uploadFile(multipartFile[0]);
                    Image image = Image.builder().imageType(ImageTypes.USER_IMG.getValue()).url(urlImg)
                            .referenceId(customerId).build();
                    Image savedImage = imageService.saveImage(image);
                } else {
                    Image image = imageList.get(0);
                    cloudinaryService.deleteFile(image.getUrl());
                    String urlImg = cloudinaryService.uploadFile(multipartFile[0]);
                    image.setUrl(urlImg);
                    imageService.saveImage(image);
                }
            }

        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return "redirect:/admin/profile-info";
    }

    @PostMapping("/change-password")
    public String changePassword(Authentication authentication
            , @ModelAttribute("password") String password
            , @ModelAttribute("new-password") String newPassword
            , @ModelAttribute("submit-password") String submitPassword

    ) {
        long accountId = Long.parseLong(authentication.getName());
        boolean result = accountService.changePassword(accountId, password, newPassword, submitPassword);
        if (result == false) {
            _log.error("cannot update pass");
        }
        return "redirect:/admin/logout";
    }
}
