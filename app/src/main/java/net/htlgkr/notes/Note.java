package net.htlgkr.notes;

import java.io.Serializable;
import java.time.LocalDate;

public class Note implements Serializable {
    private String title;
    private String notes;
    private LocalDate date;

    public Note(String title, String notes, LocalDate date){
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
