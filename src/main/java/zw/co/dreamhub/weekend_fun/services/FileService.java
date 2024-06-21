package zw.co.dreamhub.weekend_fun.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.dreamhub.weekend_fun.config.FileEnv;
import zw.co.dreamhub.weekend_fun.domain.dto.Product;
import zw.co.dreamhub.weekend_fun.domain.models.Translation;
import zw.co.dreamhub.weekend_fun.domain.repository.TranslationRepository;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marlvin Chihota
 * Email marlvinchihota@gmail.com
 * Created on 21/6/2024
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final FileEnv env;
    private final TranslationRepository translationRepository;
    private final ObjectMapper mapper;

    public void process() {

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime;

        log.info("File path : {}", env.path());
        log.info("Start Time : {}", startTime);

        try {

            File file = new File(env.path());
            Product[] products = mapper.readValue(file, Product[].class);
            log.info("Mapped : {} products", products.length);

            Arrays.asList(products).forEach(product -> {


                List<Field> fields = Arrays.asList(Product.Translations.class.getDeclaredFields());
                fields.forEach(classField -> {

                    try {

                        Field field = product.getTranslations().getClass().getDeclaredField(classField.getName());
                        field.setAccessible(true);
                        Object value = field.get(product.getTranslations());

                        Translation translation = new Translation(
                                product.getProductId(),
                                classField.getName(),
                                value.toString()
                        );
                        translationRepository.save(translation);

                    } catch (NoSuchFieldException e) {
                        log.info("No field exception : {}", e.getMessage());
                    } catch (IllegalAccessException e) {
                        log.info("Illegal access exception : {}", e.getMessage());
                    }

                });

            });

        } catch (Exception e) {
            log.info("Mapping exception : {}", e.getMessage());
        } finally {
            endTime = LocalDateTime.now();
            log.info("End Time : {}", endTime);
        }

    }


}
