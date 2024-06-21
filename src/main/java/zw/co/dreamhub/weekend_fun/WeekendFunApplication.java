package zw.co.dreamhub.weekend_fun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import zw.co.dreamhub.weekend_fun.config.FileEnv;

@SpringBootApplication
@EnableConfigurationProperties({
        FileEnv.class
})
public class WeekendFunApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeekendFunApplication.class, args);
    }

}
