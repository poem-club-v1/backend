package poem.club.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poem.club.backend.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String category);
}
