package zw.co.dreamhub.weekend_fun.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Marlvin Chihota
 * Email marlvinchihota@gmail.com
 * Created on 21/6/2024
 */

@ConfigurationProperties(prefix = "file")
public record FileEnv(
        String path
) {
}
