package com.bob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRedisApplication.class, args);
        System.out.println("====================================");
        System.out.println("=============启动成功!===============");
        System.out.println("====================================");
    }

}
