package com.bob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShiroApplication.class, args);
        System.out.println("====================================");
        System.out.println("=====springboot-shiro启动成功!======");
        System.out.println("====================================");
    }

}
