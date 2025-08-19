package com.eric.ericaicodemother;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.eric.ericaicodemother.mapper")
public class EricAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(EricAiCodeMotherApplication.class, args);
    }

}
