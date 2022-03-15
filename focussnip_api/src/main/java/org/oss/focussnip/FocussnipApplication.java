package org.oss.focussnip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
public class FocussnipApplication {

    public static void main(String[] args) {
        SpringApplication.run(FocussnipApplication.class, args);
    }

}
