package net.htlgkr.notes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note implements Serializable {
    public static final String CSV_HEADER = "TITLE;NOTES;DATETIME";
    public static final String DATE_PATTERN = "dd.MM.yyyy HH:mm";

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

    public static String serialize(Note note) {
        return note.getTitle() + ";" + note.getNotes() + ";" + DateTimeFormatter.ofPattern(DATE_PATTERN).format(note.getDate());
    }
}
