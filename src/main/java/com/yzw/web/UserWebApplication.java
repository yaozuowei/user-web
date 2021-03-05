package com.yzw.web;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Yao
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.yzw.web.core.mapper"})
/**
 * 开启加密注解
 */
@EnableEncryptableProperties
public class UserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWebApplication.class, args);
    }

}
