package com.datn.adminservice.controller;

import com.datn.commonbase.entity.Voucher;
import com.datn.commonbase.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @GetMapping("/vouchers")
    public String getVouchers(Model model) {
        Voucher voucher = Voucher.builder().voucherName("hehehe")
                .type(1)
                .start(Date.valueOf("2023-03-03"))
                .end(Date.valueOf("2023-03-25"))
                .discountPercent(15000)
                .build();
//        voucherService.saveVoucher(voucher);
        List<Voucher> voucherList = voucherService.getListVoucher(true, 0, 100);
        model.addAttribute("voucherList", voucherList);
        return "voucher/voucher-list";
    }

    @GetMapping("/add-voucher")
    public String addVoucher(Model model, @RequestParam(required = false, value = "userid", defaultValue = "-1") int userId) {
        Voucher voucher = Voucher.builder()
                .build();
        if (userId != -1) {
            voucher.setUserId(userId);
        }
//        voucherService.saveVoucher(voucher);
        model.addAttribute("voucher", voucher);
        return "voucher/add-voucher";
    }

    @PostMapping("/add-voucher")
    public String postVoucher(Model model
            , @ModelAttribute Voucher voucher
            , @ModelAttribute("selectedCategory") int type) {
//        voucherService.saveVoucher(voucher);
        voucher.setType(type);
        voucherService.saveVoucher(voucher);
        return "redirect:/admin/add-voucher";
    }

    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable("id") long voucherId) {
        Voucher voucher = voucherService.getVoucher(voucherId);
        voucher.setStatus(!voucher.isStatus());
        voucherService.saveVoucher(voucher);
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/update-voucher/{id}")
    public String updateVoucher(Model model, @PathVariable("id") long voucherId) {
        Voucher voucher = voucherService.getVoucher(voucherId);
        model.addAttribute("voucher", voucher);
        return "voucher/updateVoucher";
    }

    @PostMapping("/update-voucher/{id}")
    public String patchVoucher(Model model, Voucher voucher, @PathVariable("id") long voucherId, @ModelAttribute("selectedCategory") int type) {
        voucher.setType(type);
        model.addAttribute("voucher", voucher);
        return "redirect:/admin/update-voucher/" + voucherId;
    }

}
