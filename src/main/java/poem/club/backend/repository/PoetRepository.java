package poem.club.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poem.club.backend.entity.Poet;

import java.util.Optional;

public interface PoetRepository extends JpaRepository<Poet, Long> {
    Optional<Poet> findByEmail(String email);

    boolean existsByUsername(String name);
}
