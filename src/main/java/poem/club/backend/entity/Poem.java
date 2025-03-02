package poem.club.backend.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "poems")
public class Poem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;
    private Date dateCreated;
    private int numberOfLikes;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Poet author;

    @ManyToMany(mappedBy = "likes")
    private List<Poet> poets;

    public Poem(Long id, String title, String content, Poet author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Language getLanguage() { return language; }

    public void setLanguage(Language language) { this.language = language; }

    public Poet getAuthor() {
        return author;
    }

    public void setAuthor(Poet author) {
        this.author = author;
    }

    public List<Poet> getPoets() {
        return poets;
    }

    public void setPoets(List<Poet> poets) {
        this.poets = poets;
    }
}
