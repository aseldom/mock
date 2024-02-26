package ru.checkdev.mock;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class MockSrv {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MockSrv.class);
        application.addListeners(new ApplicationPidFileWriter("./mock.pid"));
        application.run();
    }

    @Bean
    public SpringLiquibase liquibase(DataSource ds) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/liquibase-changeLog.xml");
        liquibase.setDataSource(ds);
        return liquibase;
    }
}

