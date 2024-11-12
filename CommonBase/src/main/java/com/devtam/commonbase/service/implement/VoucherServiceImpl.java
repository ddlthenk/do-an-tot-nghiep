package com.devtam.commonbase.service.implement;

import com.devtam.commonbase.entity.Voucher;
import com.devtam.commonbase.repository.VoucherRepository;
import com.devtam.commonbase.service.VoucherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    VoucherRepository voucherRepository;
    private final Logger _log = LogManager.getLogger(VoucherServiceImpl.class);

    @Override
    public List<Voucher> getListVoucher(boolean status, int page, int limit) {
        List<Voucher> listVoucher;
        try {
            listVoucher = voucherRepository.findAllByStatusIs(status, PageRequest.of(page, limit));
        } catch (Exception e) {
            _log.error(e.getMessage());
            return Collections.emptyList();
        }
        return listVoucher;
    }

    @Override
    public List<Voucher> getListVoucherByType(int type, boolean status, int page, int limit) {
        List<Voucher> listVoucher;
        try {
            listVoucher = voucherRepository.findAllByStatusIsAndType(status, type, PageRequest.of(page, limit));
        } catch (Exception e) {
            _log.error(e.getMessage());
            return Collections.emptyList();
        }
        return listVoucher;
    }

    @Override
    public List<Voucher> getListVoucherByUserId(long userId, boolean status, int page, int limit) {
        List<Voucher> listVoucher;
        try {
            listVoucher = voucherRepository.findAllByStatusIsAndUserId(status, userId, PageRequest.of(page, limit));
        } catch (Exception e) {
            _log.error(e.getMessage());
            return Collections.emptyList();
        }
        return listVoucher;
    }

    @Override
    public List<Voucher> getListVoucherByUserIdAndAllowed(long userId, boolean status, int page, int limit) {
        List<Voucher> listVoucher;
        try {
            Date date = new Date(System.currentTimeMillis());
            listVoucher = voucherRepository
                    .findAllByStatusIsAndUserIdAndStartLessThanEqualAndEndGreaterThanEqual(status, userId, date, date, PageRequest.of(page, limit));
        } catch (Exception e) {
            _log.error(e.getMessage());
            return Collections.emptyList();
        }
        return listVoucher;
    }

    @Override
    public Voucher getVoucher(long voucherId) {
        Optional<Voucher> voucher = voucherRepository.findById(voucherId);
        if (voucher.isPresent()) {
            return voucher.get();
        }
        return null;
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }
}
