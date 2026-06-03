package com.hidrico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Para tarefas agendadas de monitoramento
public class HidricoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(HidricoApplication.class, args);
    }
}
