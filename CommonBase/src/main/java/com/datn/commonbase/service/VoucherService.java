package com.datn.commonbase.service;

import com.datn.commonbase.entity.Voucher;

import java.util.List;

public interface VoucherService {
    List<Voucher> getListVoucher(boolean status, int page, int limit);

    List<Voucher> getListVoucherByType(int type, boolean status, int page, int limit);

    List<Voucher> getListVoucherByUserId(long userId, boolean status, int page, int limit);

    public List<Voucher> getListVoucherByUserIdAndAllowed(long userId, boolean status, int page, int limit);

    Voucher getVoucher(long voucherId);

    Voucher saveVoucher(Voucher voucher);
}
