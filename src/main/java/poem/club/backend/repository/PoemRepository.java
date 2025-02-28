package poem.club.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poem.club.backend.entity.Poem;

import java.util.List;

public interface PoemRepository extends JpaRepository<Poem, Long> {
    List<Poem> findTop20ByOrderByNumberOfLikesDesc();

    List<Poem> findTop20ByOrderByDateCreatedDesc();

    List<Poem> findByPoets_Id(Long userId);
}
