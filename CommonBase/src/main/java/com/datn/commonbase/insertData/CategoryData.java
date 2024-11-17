package com.datn.commonbase.insertData;


import com.datn.commonbase.constant.ImageTypes;
import com.datn.commonbase.entity.Category;
import com.datn.commonbase.entity.Image;
import com.datn.commonbase.service.CategoryService;
import com.datn.commonbase.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryData {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ImageService imageService;

    @Bean
    CommandLineRunner addNewCategory() {
        return args -> {
            Category category = Category.builder()
                    .categoryId(1)
                    .categoryName("Thời Trang")
                    .categoryDescription("Thời Trang")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image = Image.builder().referenceId(1).imageType(ImageTypes.CATEGORY_IMG.getValue()).url("/category-img/thoitrang.png").build();
            imageService.saveImage(image);

            category = Category.builder()
                    .categoryId(2)
                    .categoryName("Sắc Đẹp Và Sức Khỏe")
                    .categoryDescription("Sắc đẹp và sức khỏe")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image2 = Image.builder().referenceId(2).url("/category-img/sacdep.png").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image2);

            category = Category.builder()
                    .categoryId(3)
                    .categoryName("Phụ Kiện Thời Trang")
                    .categoryDescription("Phụ kiện thời trang")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image3 = Image.builder().referenceId(3).url("/category-img/phukienthoitrang.png").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image3);

            category = Category.builder()
                    .categoryId(4)
                    .categoryName("Điện Thoại & Phụ Kiện")
                    .categoryDescription("Điện thoại và phụ kiện")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image4 = Image.builder().referenceId(4).url("/category-img/dienthoaiphukien.png").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image4);

            category = Category.builder()
                    .categoryId(5)
                    .categoryName("Thực phẩm và đồ uống")
                    .categoryDescription("Thực phẩm và đồ uống")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image5 = Image.builder().referenceId(5).url("/category-img/thucphamdouong.jpg").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image5);

            category = Category.builder()
                    .categoryId(6)
                    .categoryName("Mẹ & Bé")
                    .categoryDescription("Mẹ & Bé")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image6 = Image.builder().referenceId(6).url("/category-img/mevabe.png").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image6);

            category = Category.builder()
                    .categoryId(7)
                    .categoryName("Gaming & Console")
                    .categoryDescription("Gaming & Console")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image7 = Image.builder().referenceId(7).url("/category-img/gaming.jpg").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image7);

            category = Category.builder()
                    .categoryId(8)
                    .categoryName("Nhà cửa & Đời sống")
                    .categoryDescription("Nhà cửa & Đời sống")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image8 = Image.builder().referenceId(8).url("/category-img/nhacuavadoisong.png").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image8);

            category = Category.builder()
                    .categoryId(9)
                    .categoryName("Văn Phòng Phẩm")
                    .categoryDescription("Văn Phòng Phẩm")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image9 = Image.builder().referenceId(9).url("/category-img/vanphongpham.png").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image9);

            category = Category.builder()
                    .categoryId(10)
                    .categoryName("Máy tính & Laptop")
                    .categoryDescription("Máy tính & Laptop")
                    .parentId(-1)
                    .build();
            categoryService.saveCategory(category);
            Image image10 = Image.builder().referenceId(10).url("/category-img/maytinh.png").imageType(ImageTypes.CATEGORY_IMG.getValue()).build();
            imageService.saveImage(image10);
        };
    }
}
