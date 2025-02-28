package poem.club.backend.dto;

import java.util.Date;

public class NewPoemDto {

    private String title;
    private String content;

    public NewPoemDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NewPoemDto() {
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
}
