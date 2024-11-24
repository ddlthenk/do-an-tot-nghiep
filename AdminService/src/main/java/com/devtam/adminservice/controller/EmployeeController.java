package com.devtam.adminservice.controller;

import com.devtam.adminservice.common.PasswordHandler;
import com.devtam.commonbase.constant.AccountState;
import com.devtam.commonbase.constant.DefaultValue;
import com.devtam.commonbase.constant.ImageTypes;
import com.devtam.commonbase.entity.Account;
import com.devtam.commonbase.entity.Image;
import com.devtam.commonbase.entity.User;
import com.devtam.commonbase.service.AccountService;
import com.devtam.commonbase.service.ImageService;
import com.devtam.commonbase.service.UserService;
import com.devtam.commonbase.util.RoleConstant;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/employee")
public class EmployeeController {
    private final Logger _log = LogManager.getLogger(EmployeeController.class);
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;
    @Autowired
    ImageService imageService;
    @Autowired
    HttpSession session;

    @GetMapping({"/", ""})
    public String getListEmployees(Authentication authentication, Model model) {
        try {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(RoleConstant.getObjectName(RoleConstant.ROOT.getValue()));
            if (authentication.getAuthorities().contains(simpleGrantedAuthority)) {
                List<Long> accountIds = accountService.getAccountIds(RoleConstant.ADMIN.getValue(), DefaultValue.DEFAULT_LIMIT, AccountState.ACTIVE);
                List<User> empoyeeList = userService.getUsersByListIds(accountIds);
                Map<Long, List<Image>> imageMap = imageService.getMapImage(accountIds, ImageTypes.USER_IMG.getValue());
                model.addAttribute("employees", empoyeeList);
                model.addAttribute("imageMap", imageMap);
            }
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "redirect:/admin/404";
        }
        return "employee/list-employee";
    }

    @GetMapping("/add")
    public String createEmployee(Authentication authentication, Model model) {
        try {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(RoleConstant.getObjectName(RoleConstant.ROOT.getValue()));
            if (authentication.getAuthorities().contains(simpleGrantedAuthority)) {
                return "employee/add-employee";
            }
            return "redirect:/admin/access-denied";
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "redirect:/admin/404";
        }
    }

    @PostMapping("/add")
    public String creatingEmployee(Authentication authentication, Model model
            , @ModelAttribute("fullname") String fullname
            , @ModelAttribute("username") String username
            , @ModelAttribute("email") String email
            , @ModelAttribute("phonenumber") String phonenumber
    ) {
        session.removeAttribute("create_account_error");
        Account account = Account.builder()
                .email(email)
                .phoneNumber(phonenumber)
                .userName(username)
                .role(RoleConstant.ADMIN.getValue())
                .password(PasswordHandler.hashPassword("123456"))
                .build();
        Account saveAccount = accountService.saveAccount(account);
        User user = User.builder()
                .userId(account.getAccountId())
                .name(fullname)
                .language("vi")
                .build();
        User saveUser = userService.saveUser(user);
        if (saveAccount != null && saveUser != null) {
            return "redirect:/admin/employee";
        }
        session.setAttribute("create_account_error", "Cannot add new account!!!");
        return "redirect:/admin/employee/create-employee";
    }

    @GetMapping("/info/{id}")
    public String employeeInfo(@PathVariable("id") long id, Model model) {
        try {
            User user = userService.getUserById(id);
            Account account = accountService.getAccountById(id);
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
            model.addAttribute("imageUrl", imageUrl);
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return "employee/employee-info";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") long id, Model model) {
        try {
            userService.deleteUser(id);
            accountService.deleteAccount(id);
            return "redirect:/admin/employee";
        } catch (Exception e) {
            _log.error(e.getMessage());
            model.addAttribute("error", "Failed to delete employee");
            return "employee/list-employee";
        }
    }

    @GetMapping("/test")
    public String testUI(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername(); // Lấy userId từ UserDetails
            User user = userService.getUserById(Long.valueOf(userId));
            if (user.getName() == null) {
                user.setName("");
            }
            model.addAttribute("user", user);
            session.setAttribute("create_account_error", "test nhá");
            session.removeAttribute("create_account_error");
        }
        return "employee/add-employee";
    }

    @ResponseBody
    @GetMapping("/checkUser/{id}")
    private String checkId(@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return user.getName();
        }
        return "";
    }
}
