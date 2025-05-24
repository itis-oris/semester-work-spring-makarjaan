package com.makarova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Arrays;


@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
