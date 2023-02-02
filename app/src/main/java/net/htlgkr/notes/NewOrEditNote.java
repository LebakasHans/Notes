package net.htlgkr.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class NewOrEditNote extends AppCompatActivity {
    private Note noteFromBundle;
    private FloatingActionButton addButton;
    private CalendarView date;
    private EditText title;
    private EditText notes;
    private TextView currentlyPickedTime;
    private LocalDateTime dateTime;
    private Intent extras;
    private boolean edit = false;
    private boolean detail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_or_edit_note);

        currentlyPickedTime = findViewById(R.id.currentlyPickedTime);
        title = findViewById(R.id.titleTxt);
        notes = findViewById(R.id.notesTxt);
        setUpDate();
        setUpAddButton();

        extras = getIntent();
        if(extras.getExtras() != null){
            if(extras.hasExtra("EDIT")){
                noteFromBundle = (Note) extras.getSerializableExtra("EDIT");
                edit = true;
            }else if(extras.hasExtra("DETAILS")){
                noteFromBundle = (Note) extras.getSerializableExtra("DETAILS");
                detail = true;
            }
        }

        if (edit || detail){
            title.setText(noteFromBundle.getTitle());
            notes.setText(noteFromBundle.getNotes());
            date.setDate(
                    Date.from(noteFromBundle.getDate()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant())
                            .getTime()
            );
            currentlyPickedTime.setText(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(noteFromBundle.getDate()));
        }
        if (detail){
            title.setEnabled(false);
            notes.setEnabled(false);
            // Note: I didn't find a way to disable a CalendarView
            date.setVisibility(View.GONE);
            addButton.setEnabled(false);
            addButton.setVisibility(View.GONE);
        }
    }

    private void setUpAddButton() {
        addButton = findViewById(R.id.finishedNoteBtd);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty() || !notes.getText().toString().isEmpty()) {
                    if (dateTime == null) {
                        dateTime = LocalDateTime.now();
                    }
                    Note newNote = new Note(title.getText().toString(), notes.getText().toString(), dateTime);
                    sendNewNoteToMain(newNote);
                } else {
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
                            currentlyPickedTime.setText(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(dateTime));
                        }
                    },LocalTime.now().getHour(), LocalTime.now().getMinute(), true)
                    .show();
            }
        });
    }

    private void sendNewNoteToMain(Note newNote) {
        Intent intent = new Intent("New-Note");
        intent.putExtra("newNote", newNote);
        intent.putExtra("isEdit", edit);
        if (edit){
            intent.putExtra("position", intent.getIntExtra("POSITION", -1));
        }

        intent.putExtra("isDetail", detail);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }
}