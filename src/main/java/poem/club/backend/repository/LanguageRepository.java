package poem.club.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poem.club.backend.entity.Language;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByName(String language);
}
