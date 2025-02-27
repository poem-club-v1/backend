package poem.club.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poem.club.backend.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
