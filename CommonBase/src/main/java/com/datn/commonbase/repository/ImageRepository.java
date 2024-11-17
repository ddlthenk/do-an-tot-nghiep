package com.datn.commonbase.repository;


import com.datn.commonbase.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    public List<Image> getAllByReferenceIdAndImageType(long referenceId, long imageType);
}
