package poem.club.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "poems")
public class Poem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    public Poem(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Poem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
