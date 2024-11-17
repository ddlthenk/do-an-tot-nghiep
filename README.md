Java Spring Boot application base on Microservice structure \
File báo cáo: https://1drv.ms/w/s!Al0aIDbXYuPo7UFzeNm9DuWrV823?e=XqzG4N

Chia project thành 3 module

- CommonBase: module dùng chung
- AdminService và UserService là xử lý nghiệp vụ riêng cho admin và user

Các bước xây dựng dự án:

- Khởi tạo dự án
- tạo các entity(thực thể) theo bảng đã thiết kế
- tạo môi trường, cài đặt mysql server
- tạo repository layer: kết nối và tương tác với database
- service là xử lý các logic nghiệp vụ
- insertData là để fake data khi khởi chạy dự án
- constant là các hằng số dùng chung
- configuration là cấu hình cho dự án
- common là các lớp xử lý logic dùng chung
- util là các lớp công cụ