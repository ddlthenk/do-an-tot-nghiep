package com.devtam.adminservice;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.util.HashMap;
import java.util.Map;


@ComponentScan(basePackages = {"com.devtam.adminservice", "com.devtam.commonbase"})
@SpringBootApplication
public class AdminServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }


}
