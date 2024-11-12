package com.devtam.commonbase;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CommonBaseApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CommonBaseApplication.class, args);
    }


}
