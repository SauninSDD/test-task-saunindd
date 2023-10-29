package ru.sovkom.backend;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

}
