package poem.club.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "poets")
public class Poet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String username;

    @OneToMany(mappedBy = "author")
    private List<Poem> poems;

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "poet_id"),
            inverseJoinColumns = @JoinColumn(name = "poem_id")
    )
    private List<Poem> likes;

    public Poet(Long id, String name, String email, String username) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
    }

    public Poet() {
    }

    public List<Poem> getLikes() {
        return likes;
    }

    public void setLikes(List<Poem> likes) {
        this.likes = likes;
    }

    public List<Poem> getPoems() {
        return poems;
    }

    public void setPoems(List<Poem> poems) {
        this.poems = poems;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
