package zw.co.dreamhub.weekend_fun.initialization;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zw.co.dreamhub.weekend_fun.services.FileService;

/**
 * @author Marlvin Chihota
 * Email marlvinchihota@gmail.com
 * Created on 21/6/2024
 */

@Component
@RequiredArgsConstructor
public class Initialization implements CommandLineRunner {

    private final FileService service;

    @Override
    public void run(String... args) {
        service.process();
    }
}
