package ua.foxminded.schoolconsoleapp;

import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchoolConsoleAppApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(
        SchoolConsoleAppApplication.class, args);

    SchoolFrontController frontController = context.getBean(SchoolFrontController.class);

    frontController.run();
  }

  @Bean
  public Scanner scanner() {
    return new Scanner(System.in);
  }

}
