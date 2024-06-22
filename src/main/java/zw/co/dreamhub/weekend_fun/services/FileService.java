package zw.co.dreamhub.weekend_fun.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.dreamhub.weekend_fun.config.FileEnv;
import zw.co.dreamhub.weekend_fun.domain.dto.Product;
import zw.co.dreamhub.weekend_fun.domain.models.Translation;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final ObjectMapper mapper;
    private final HikariDataSource hikariDataSource;

    public void process() {

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime loadDone;
        LocalDateTime endTime;

        log.info("File path : {}", env.path());
        log.info("Start Time : {}", startTime);

        try {

            File file = new File(env.path());
            Product[] products = mapper.readValue(file, Product[].class);
            loadDone = LocalDateTime.now();
            log.info("Loaded : {} products in {} seconds",
                    products.length,
                    ChronoUnit.SECONDS.between(startTime, loadDone));

            String query = String.format("INSERT INTO %s (id, created_at, product_id, lang_code, text) VALUES(?, ?, ?, ?, ?)"
                    , Translation.class.getAnnotation(Table.class).name());
            AtomicInteger id = new AtomicInteger(1);

            try (
                    Connection connection = hikariDataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement(query)
            ) {
                Arrays.stream(products).forEach(product -> {
                    List<Field> fields = Arrays.asList(Product.Translations.class.getDeclaredFields());
                    fields.forEach(classField -> {
                        try {
                            Field field = product.getTranslations().getClass().getDeclaredField(classField.getName());
                            field.setAccessible(true);
                            Object value = field.get(product.getTranslations());
                            statement.clearParameters();
                            statement.setInt(1, id.get());
                            statement.setTimestamp(2, Timestamp.from(Instant.now()));
                            statement.setInt(3, product.getProductId());
                            statement.setString(4, classField.getName());
                            statement.setString(5, value.toString());
//                            statement.executeUpdate();
                            statement.addBatch();
                            id.addAndGet(1);
                        } catch (NoSuchFieldException e) {
                            log.info("No field exception : {}", e.getMessage());
                        } catch (IllegalAccessException e) {
                            log.info("Illegal access exception : {}", e.getMessage());
                        } catch (SQLException e) {
                            log.info("SQL exception : {}", e.getMessage());
                        }
                    });
                });
                statement.executeBatch();
            }

            log.info("Saved : {} products in : {} seconds",
                    products.length,
                    ChronoUnit.SECONDS.between(startTime, LocalDateTime.now()));

        } catch (Exception e) {
            log.info("Mapping exception : {}", e.getMessage());
        } finally {
            endTime = LocalDateTime.now();
            log.info("End Time : {}", endTime);
            log.info("Processing Time : {} seconds", ChronoUnit.SECONDS.between(startTime, endTime));
        }

    }

}
