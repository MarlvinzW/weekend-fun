package zw.co.dreamhub.weekend_fun.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * @author Marlvin Chihota
 * Email marlvinchihota@gmail.com
 * Created on 21/6/2024
 */

@Entity
@Table(name = "translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "lang_code")
    private String languageCode;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    public Translation(
            int productId,
            String languageCode,
            String text
    ) {
        this.productId = productId;
        this.languageCode = languageCode;
        this.text = text;
    }
}
