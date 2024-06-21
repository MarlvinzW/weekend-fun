package zw.co.dreamhub.weekend_fun.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.dreamhub.weekend_fun.domain.models.Translation;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Integer> {
}