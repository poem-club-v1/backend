package poem.club.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poem.club.backend.entity.Poet;

public interface PoetRepository extends JpaRepository<Poet, Long> {
}
