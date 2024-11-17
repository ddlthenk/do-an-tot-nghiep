package com.datn.commonbase.service;


import com.datn.commonbase.entity.Image;

import java.util.List;
import java.util.Map;

public interface ImageService {
    public List<Image> getListImage(long referenceId, long imageType);

    public Map<Long, List<Image>> getMapImage(List<Long> listIds, long imageType);

    public Map<Long, Image> getMapOneImage(List<Long> listIds, long imageType);

    public boolean insertImage();

    public Image saveImage(Image image);

    public Image getImage(long id);

    public boolean deleteImage(Image image);

}
