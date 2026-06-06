package com.shea.ioc.aiapplicationcreator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy=true)
public class AiApplicationCreatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplicationCreatorApplication.class, args);
    }

}
