package com.devtam.commonbase.repository;

import com.devtam.commonbase.entity.Voucher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findAllByStatusIs(boolean status, Pageable pageRequest);

    List<Voucher> findAllByStatusIsAndType(boolean status, int type, Pageable pageRequest);

    List<Voucher> findAllByStatusIsAndUserId(boolean status, long userId, Pageable pageRequest);

    List<Voucher> findAllByStatusIsAndUserIdAndStartLessThanEqualAndEndGreaterThanEqual(boolean status, long userId, Date dateNow, Date dateNow2, Pageable pageRequest);
}
