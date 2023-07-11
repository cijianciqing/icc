package com.nanshan.icc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@Slf4j
@MapperScan({"com.nanshan.icc.generated.dao"
})
public class IccApplication {

    public static void main(String[] args) {
        SpringApplication.run(IccApplication.class, args);
    }

    @GetMapping("/test")
    public String test(){
        log.info("ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
        return "hello world!";
    }
}
