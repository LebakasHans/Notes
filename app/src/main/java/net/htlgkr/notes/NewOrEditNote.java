package net.htlgkr.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class NewOrEditNote extends AppCompatActivity {
    private FloatingActionButton addButton;
    private CalendarView date;
    private EditText title;
    private EditText notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_or_edit_note);

        date = findViewById(R.id.noteDate);
        title = findViewById(R.id.titleTxt);
        notes = findViewById(R.id.notesTxt);

        addButton = findViewById(R.id.finishedNoteBtd);

        addButton.setOnClickListener(v -> {
            LocalDate newNoteDate = new Date(date.getDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            Note newNote = new Note(title.getText().toString(), notes.getText().toString(), newNoteDate);
            sendNewNoteToMain(newNote);
        });
    }

    private void sendNewNoteToMain(Note newNote) {
        Intent intent = new Intent("New-Note");
        intent.putExtra("newNote", newNote);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }
}