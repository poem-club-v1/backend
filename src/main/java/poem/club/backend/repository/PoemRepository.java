package poem.club.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poem.club.backend.entity.Poem;

import java.util.List;

public interface PoemRepository extends JpaRepository<Poem, Long> {
    List<Poem> findTop20ByOrderByNumberOfLikesDesc();

    List<Poem> findTop20ByOrderByDateCreatedDesc();

    List<Poem> findByPoets_Id(Long userId);

    @Query("SELECT p FROM Poem p " +
            "JOIN p.language l " +
            "JOIN p.category c " +
            "JOIN p.author a " +
            "WHERE (:category IS NULL OR c.name = :category) " +
            "AND (:language IS NULL OR l.name = :language) " +
            "AND (:author IS NULL OR a.username = :author) ")
    List<Poem> findFilteredPoems(
            @Param("category") String category,
            @Param("language") String language,
            @Param("author") String author
    );

}
