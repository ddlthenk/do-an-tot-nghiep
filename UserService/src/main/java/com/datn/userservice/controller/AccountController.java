package com.datn.userservice.controller;

import com.datn.adminservice.common.PasswordHandler;
import com.datn.commonbase.constant.DefaultValue;
import com.datn.commonbase.constant.ImageTypes;
import com.datn.commonbase.entity.Account;
import com.datn.commonbase.entity.Category;
import com.datn.commonbase.entity.Image;
import com.datn.commonbase.entity.User;
import com.datn.commonbase.service.*;
import com.datn.commonbase.util.RoleConstant;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class AccountController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    HttpSession session;
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;
    private CloudinaryService cloudinaryService = new CloudinaryService();
    private final Logger _log = LogManager.getLogger(AccountController.class);

    @GetMapping("/login")
    public String loginUser(Authentication authentication, @RequestParam(value = "error", defaultValue = "") String error, Model model
            , @RequestParam(value = "login-error", defaultValue = "") String loginError) {
        getCategoryParentList();
        getUserData(authentication);
        if (error.equals("true")) {
            model.addAttribute("error", "true");
        }
        if (loginError.equals("true")) {
            model.addAttribute("loginError", "true");
        }
        if (authentication != null) {
            return "redirect:/home?logged-in=true";
        }
        return "account/login";
    }

    @GetMapping("/sign-up")
    public String signUpUser(Authentication authentication, Model model, @RequestParam(value = "error", defaultValue = "") String error) {
        if (authentication != null) {
            return "redirect:/sign-up?logged-in=true";
        }
        model.addAttribute("error", error);
        getCategoryParentList();
        getUserData(authentication);
        return "account/sign-up";
    }

    @PostMapping("sign-up")
    public String registerNewUser(@ModelAttribute("full-name") String fullName, @ModelAttribute("email") String email
            , @ModelAttribute("phone-number") String phoneNumber, @ModelAttribute("password") String password, @ModelAttribute("date-of-birth") Date dateOfBirth) {
        Account account = Account.builder().email(email).phoneNumber(phoneNumber)
                .password(PasswordHandler.hashPassword(password))
                .role(RoleConstant.USER.getValue()).build();
        if (accountService.isExist(account)) {
            return "redirect:/sign-up?error=true";
        }
        Account savedAccount = accountService.saveAccount(account);
        User user = User.builder().userId(savedAccount.getAccountId()).dateOfBirth(dateOfBirth).name(fullName).build();
        User savedUser = userService.saveUser(user);
        return "redirect:/login?success=true";
    }

    @GetMapping("/personal-information")
    public String personalInformation(Authentication authentication, Model model
            , @RequestParam(value = "changePass", defaultValue = "") String changePass
            , @RequestParam(value = "changeInfo", defaultValue = "") String changeInfo
    ) {
        try {
            if (authentication == null) {
                return "redirect:/login?error=true";
            }
            model.addAttribute("changePass", changePass);
            model.addAttribute("changeInfo", changeInfo);
            getCategoryParentList();
            getUserData(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername();
            User user = userService.getUserById(Long.parseLong(userId));
            if (user != null) {
                model.addAttribute("user", user);
                System.out.println(user.getName());
            }
            Account account = accountService.getAccountById(Long.parseLong(userId));
            if (account != null) {
                model.addAttribute("account", account);
            }
            String imageUrl = DefaultValue.DEFAULT_IMAGE_URL;
            List<Image> imageDtos = imageService.getListImage(Long.parseLong(userId), ImageTypes.USER_IMG.getValue());
            if (imageDtos != null && !imageDtos.isEmpty()) {
                imageUrl = imageDtos.get(0).getUrl();
            }
            model.addAttribute("imageUrl", imageUrl);
            return "account/personal";
        } catch (Exception e) {
            _log.error(e);
            return "redirect:/error";
        }

    }

    @PostMapping("/personal-information")
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
            return "redirect:/personal-information?changeInfo=true";
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "redirect:/personal-information?changeInfo=false";
        }

    }

    @PostMapping("/change-password")
    public String changePassword(Authentication authentication
            , @ModelAttribute("oldPassword") String oldPassword
            , @ModelAttribute("newPassword") String newPassword
            , @ModelAttribute("confirmPassword") String confirmPassword

    ) {
        long accountId = Long.parseLong(authentication.getName());
        boolean result = accountService.changePassword(accountId, oldPassword, newPassword, confirmPassword);
        if (result == false) {
            _log.error("cannot update pass");
            return "redirect:/personal-information?changePass=false";
        }
        return "redirect:/personal-information?changePass=true";
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
