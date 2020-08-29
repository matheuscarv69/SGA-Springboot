package io.github.matheuscarv69;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class SgaApplication {

    public static void main(String[] args) {

        SpringApplication.run(SgaApplication.class, args);
    }
}
