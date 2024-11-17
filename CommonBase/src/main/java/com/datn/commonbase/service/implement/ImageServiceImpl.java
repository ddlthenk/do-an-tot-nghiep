package com.datn.commonbase.service.implement;

import com.datn.commonbase.entity.Image;
import com.datn.commonbase.repository.ImageRepository;
import com.datn.commonbase.service.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageRepository imageRepository;
    private final Logger _log = LogManager.getLogger(ImageServiceImpl.class);

    @Override
    public List<Image> getListImage(long referenceId, long imageType) {
        try {
            List<Image> imageList = imageRepository.getAllByReferenceIdAndImageType(referenceId, imageType);
            return imageList;
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public Map<Long, List<Image>> getMapImage(List<Long> listIds, long imageType) {
        Map<Long, List<Image>> result = new HashMap<>();
        try {
            for (long id : listIds) {
                List<Image> imageList = imageRepository.getAllByReferenceIdAndImageType(id, imageType);
                if (imageList != null && !imageList.isEmpty()) {
                    result.put(id, imageList);
                }
            }
            return result;
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<Long, Image> getMapOneImage(List<Long> listIds, long imageType) {
        Map<Long, Image> result = new HashMap<>();
        try {
            for (long id : listIds) {
                List<Image> imageList = imageRepository.getAllByReferenceIdAndImageType(id, imageType);
                if (imageList != null && !imageList.isEmpty()) {
                    result.put(id, imageList.get(0));
                }
            }
            return result;
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return Collections.emptyMap();
    }

    @Override
    public boolean insertImage() {
        return false;
    }

    @Override
    public Image saveImage(Image image) {
        imageRepository.save(image);
        return image;
    }

    @Override
    public Image getImage(long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent())
            return image.get();
        else return null;
    }

    public boolean deleteImage(Image image) {
        try {
            imageRepository.delete(image);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
