package org.example;

import org.example.service.BOT;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // SpringApplication.run(Main.class, args);

        BOT.python_exec();
    }

}