package JavaApp;

import JavaApp.EmailService.EmailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.*;

@SpringBootApplication
public class JavaAppApplication {

    public static void main(String[] args) {
        File[] selectedFiles = EmailService.selectFiles();
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(() -> {
            if (selectedFiles.length > 0) {
                List<String> emailList = EmailService.emails();
                for (String email : emailList) {
                    EmailService.sendApplicationEmail(email, selectedFiles);
                }
            } else {
                System.out.println("No files selected. Emails not sent.");
            }
//        }, 0, 5, TimeUnit.SECONDS);
        SpringApplication.run(JavaAppApplication.class, args);
    }
}
