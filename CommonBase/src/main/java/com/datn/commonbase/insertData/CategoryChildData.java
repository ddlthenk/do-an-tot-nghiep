package com.datn.commonbase.insertData;


import com.datn.commonbase.entity.Category;
import com.datn.commonbase.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryChildData {
    @Autowired
    private CategoryService categoryService;

    @Bean
    CommandLineRunner addNewSubCategory_1() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Thời Trang");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Áo")
                    .categoryDescription("Áo")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Quần")
                    .categoryDescription("Quần")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Váy")
                    .categoryDescription("Váy")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Giày & Dép")
                    .categoryDescription("Giày & Dép")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Túi Xách")
                    .categoryDescription("Túi Xách")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Áo khoác")
                    .categoryDescription("Áo khoác")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }

    @Bean
    CommandLineRunner addNewSubCategory_2() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Sắc Đẹp Và Sức Khỏe");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Mỹ phẩm")
                    .categoryDescription("Mỹ phẩm")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Chăm sóc da")
                    .categoryDescription("Chăm sóc da")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Chăm sóc tóc")
                    .categoryDescription("Chăm sóc tóc")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Chăm sóc cơ thể")
                    .categoryDescription("Chăm sóc cơ thể")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Chăm sóc răng miệng")
                    .categoryDescription("Chăm sóc răng miệng")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Chăm sóc cá nhân")
                    .categoryDescription("Chăm sóc cá nhân")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Trang điểm")
                    .categoryDescription("Trang điểm")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Nước hoa")
                    .categoryDescription("Nước hoa")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Thực phẩm chức năng")
                    .categoryDescription("Thực phẩm chức năng")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }

    @Bean
    CommandLineRunner addNewSubCategory_3() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Phụ Kiện Thời Trang");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Nhẫn")
                    .categoryDescription("Nhẫn")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Bông Tai")
                    .categoryDescription("Bông Tai")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Vòng Cổ")
                    .categoryDescription("Vòng Cổ")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Dây Chuyền")
                    .categoryDescription("Dây Chuyền")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Kính Mắt")
                    .categoryDescription("Kính Mắt")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Mũ")
                    .categoryDescription("Mũ")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }

    @Bean
    CommandLineRunner addNewSubCategory_4() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Điện Thoại & Phụ Kiện");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Thẻ sim")
                    .categoryDescription("Thẻ sim")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Điện thoại di động")
                    .categoryDescription("Điện thoại di động")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Phụ kiện điện thoại")
                    .categoryDescription("Phụ kiện điện thoại")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Máy tính bảng")
                    .categoryDescription("Máy tính bảng")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Thiết bị đeo thông minh")
                    .categoryDescription("Thiết bị đeo thông minh")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }

    @Bean
    CommandLineRunner addNewSubCategory_5() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Thực phẩm và đồ uống");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Thực phẩm")
                    .categoryDescription("Thực phẩm")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Đồ uống")
                    .categoryDescription("Đồ uống")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            //convert this to Category object
            category = Category.builder()
                    .categoryName("Đồ chế biến sẵn")
                    .categoryDescription("Đồ chế biến sẵn")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Đồ ăn vặt")
                    .categoryDescription("Đồ ăn vặt")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Nhu yếu phẩm")
                    .categoryDescription("Nhu yếu phẩm")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Nguyên liệu nấu ăn")
                    .categoryDescription("Nguyên liệu nấu ăn")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Đồ làm bánh")
                    .categoryDescription("Đồ làm bánh")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Các loại bánh")
                    .categoryDescription("Các loại bánh")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }

    @Bean
    CommandLineRunner addNewSubCategory_6() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Mẹ & Bé");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Đồ dùng du lịch cho bé")
                    .categoryDescription("Đồ dùng du lịch cho bé")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Đồ dùng ăn dặm cho bé")
                    .categoryDescription("Đồ dùng ăn dặm cho bé")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Phụ kiện cho mẹ")
                    .categoryDescription("Phụ kiện cho mẹ")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Chăm sóc sức khỏe mẹ")
                    .categoryDescription("Chăm sóc sức khỏe mẹ")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Đồ dùng phòng tắm & Chăm sóc cơ thể bé")
                    .categoryDescription("Đồ dùng phòng tắm & Chăm sóc cơ thể bé")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Đồ dùng phòng ngủ cho bé")
                    .categoryDescription("Đồ dùng phòng ngủ cho bé")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("An toàn cho bé")
                    .categoryDescription("An toàn cho bé")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Sữa công thức & Thực phẩm cho bé")
                    .categoryDescription("Sữa công thức & Thực phẩm cho bé")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Chăm sóc sức khỏe bé")
                    .categoryDescription("Chăm sóc sức khỏe bé")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }

    @Bean
    CommandLineRunner addNewSubCategory_7() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Gaming & Console");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Console")
                    .categoryDescription("Console")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Phụ kiện console")
                    .categoryDescription("Phụ kiện console")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Video Games")
                    .categoryDescription("Video Games")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Khác")
                    .categoryDescription("Khác")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }

    @Bean
    CommandLineRunner addNewSubCategory_8() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Nhà cửa & Đời sống");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Dụng cụ chăm sóc nhà cửa")
                    .categoryDescription("Dụng cụ chăm sóc nhà cửa")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Dụng cụ nhà bếp")
                    .categoryDescription("Dụng cụ nhà bếp")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Bộ đồ bàn ăn")
                    .categoryDescription("Bộ đồ bàn ăn")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Đèn")
                    .categoryDescription("Đèn")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Bảo hộ gia đình")
                    .categoryDescription("Bảo hộ gia đình")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Sắp xếp nhà cửa")
                    .categoryDescription("Sắp xếp nhà cửa")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Trang trí tiệc tùng")
                    .categoryDescription("Trang trí tiệc tùng")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Đồ thờ cúng, đồ phong thủy")
                    .categoryDescription("Đồ thờ cúng, đồ phong thủy")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Khác")
                    .categoryDescription("Khác")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };

    }


    @Bean
    CommandLineRunner addNewSubCategory_9() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Văn Phòng Phẩm");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Quà Tặng - Giấy Gói")
                    .categoryDescription("Quà Tặng - Giấy Gói")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Bút các loại")
                    .categoryDescription("Bút các loại")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Thiết bị trường học")
                    .categoryDescription("Thiết bị trường học")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Họa cụ")
                    .categoryDescription("Họa cụ")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Sổ & Giấy các loại")
                    .categoryDescription("Sổ & Giấy các loại")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Thư tín")
                    .categoryDescription("Thư tín")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Sách")
                    .categoryDescription("Sách")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("E-Books")
                    .categoryDescription("E-Books")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Khác")
                    .categoryDescription("Khác")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }

    @Bean
    CommandLineRunner addNewSubCategory_10() {
        return args -> {
            Category parent = categoryService.getCategoryByName("Máy tính & Laptop");
            if (parent == null) {
                return;
            }
            Category category = Category.builder()
                    .categoryName("Máy Tính Bàn")
                    .categoryDescription("Máy Tính Bàn")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Màn Hình")
                    .categoryDescription("Màn Hình")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Linh Kiện Máy Tính")
                    .categoryDescription("Linh Kiện Máy Tính")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Thiết Bị Lưu Trữ")
                    .categoryDescription("Thiết Bị Lưu Trữ")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Thiết Bị Mạng")
                    .categoryDescription("Thiết Bị Mạng")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Phần Mềm")
                    .categoryDescription("Phần Mềm")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Thiết Bị Văn Phòng")
                    .categoryDescription("Thiết Bị Văn Phòng")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Máy In & Máy Scan")
                    .categoryDescription("Máy In & Máy Scan")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Phụ Kiện Máy Tính")
                    .categoryDescription("Phụ Kiện Máy Tính")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Chuột & Bàn Phím")
                    .categoryDescription("Chuột & Bàn Phím")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Laptop")
                    .categoryDescription("Laptop")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
            category = Category.builder()
                    .categoryName("Phụ Kiện Máy Tính Khác")
                    .categoryDescription("Phụ Kiện Máy Tính Khác")
                    .parentId(parent.getCategoryId())
                    .build();
            categoryService.saveCategory(category);
        };
    }
}
