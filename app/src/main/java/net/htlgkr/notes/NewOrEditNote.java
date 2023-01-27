package net.htlgkr.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class NewOrEditNote extends AppCompatActivity {
    private FloatingActionButton addButton;
    private CalendarView date;
    private EditText title;
    private EditText notes;
    LocalDateTime dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_or_edit_note);

        setUpDate();
        title = findViewById(R.id.titleTxt);
        notes = findViewById(R.id.notesTxt);
        setUpAddButton();

        /*
        addButton.setOnClickListener(v -> {
            LocalDate newNoteDate = new Date(date.getDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            Note newNote = new Note(title.getText().toString(), notes.getText().toString(), newNoteDate);
            sendNewNoteToMain(newNote);
        });
         */
    }

    private void setUpAddButton() {
        addButton = findViewById(R.id.finishedNoteBtd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty() || !notes.getText().toString().isEmpty()) {
                    if (dateTime == null){
                        dateTime = LocalDateTime.now();
                    }
                    Note newNote = new Note(title.getText().toString(), notes.getText().toString(), dateTime);
                    sendNewNoteToMain(newNote);
                }else {
                    Toast.makeText(NewOrEditNote.this, "All the fields have to be filled!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpDate() {
        date = findViewById(R.id.noteDate);
        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                new TimePickerDialog(NewOrEditNote.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            LocalDate date = LocalDate.of(year, month+1, dayOfMonth);
                            LocalTime time = LocalTime.of(hourOfDay, minute);
                            dateTime = LocalDateTime.of(date, time);
                            ((TextView) findViewById(R.id.currentlyPickedTime)).setText(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(dateTime));
                        }
                    },LocalTime.now().getHour(), LocalTime.now().getMinute(), true)
                    .show();
            }
        });
    }

    private void sendNewNoteToMain(Note newNote) {
        Intent intent = new Intent("New-Note");
        intent.putExtra("newNote", newNote);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }
}