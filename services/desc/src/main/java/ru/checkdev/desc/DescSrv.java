package ru.checkdev.desc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class DescSrv {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DescSrv.class);
        application.addListeners(new ApplicationPidFileWriter("./desc.pid"));
        application.run();
    }
}

