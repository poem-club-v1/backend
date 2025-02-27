package poem.club.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poem.club.backend.entity.Poem;

public interface PoemRepository extends JpaRepository<Poem, Long> {
}
