package com.bob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQApplication.class, args);
        System.out.println("==============================");
        System.out.println("===========启动成功============");
        System.out.println("==============================");
    }
}
