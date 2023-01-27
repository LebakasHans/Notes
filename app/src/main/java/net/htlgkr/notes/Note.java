package net.htlgkr.notes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Note implements Serializable {
    private String title;
    private String notes;
    private LocalDateTime date;

    public Note(String title, String notes, LocalDateTime date){
        this.title = title;
        this.notes = notes;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
