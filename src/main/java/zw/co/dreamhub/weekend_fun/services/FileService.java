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
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

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

    private void persist(PreparedStatement statement,
                         Product product,
                         Field classField,
                         Object value) throws SQLException {
//        statement.clearParameters();
        statement.setTimestamp(1, Timestamp.from(Instant.now()));
        statement.setInt(2, product.getProductId());
        statement.setString(3, classField.getName());
        statement.setString(4, value.toString());
//                            statement.executeUpdate();
        statement.addBatch();
    }


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

            String query = String.format("INSERT INTO %s (created_at, product_id, lang_code, text) VALUES(?, ?, ?, ?)"
                    , Translation.class.getAnnotation(Table.class).name());

            Connection connection = null;
            ReentrantLock lock = new ReentrantLock();
            try {
                connection = hikariDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                Stream.of(products).parallel().forEach(product -> {
                    List<Field> fields = Arrays.asList(Product.Translations.class.getDeclaredFields());
                    fields.forEach(classField -> {
                        try {
                            Field field = product.getTranslations().getClass().getDeclaredField(classField.getName());
                            field.setAccessible(true);
                            Object value = field.get(product.getTranslations());
                            lock.lock();
                            persist(statement, product, classField, value);
                            lock.unlock();
                        } catch (NoSuchFieldException e) {
                            log.info("No field exception : {}", e.getMessage());
                        } catch (IllegalAccessException e) {
                            log.info("Illegal access exception : {}", e.getMessage());
                        } catch (SQLException e) {
                            log.info("SQL inner exception : {}", e.getMessage());
                        }
                    });
                });
                statement.executeBatch();
            }
            catch (SQLException e) {
                log.info("SQL main exception : {}", e.getMessage());
            }
            finally {
                if(connection != null){
                    if(!connection.isClosed()){
                        connection.close();
                    }
                }
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
