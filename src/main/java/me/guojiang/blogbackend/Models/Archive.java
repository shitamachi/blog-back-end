package me.guojiang.blogbackend.Models;

import java.time.LocalDateTime;

public class Archive {
    private String title;
    private LocalDateTime createDateTime;

    public Archive(String title, LocalDateTime createDateTime) {
        this.title = title;
        this.createDateTime = createDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }
}
