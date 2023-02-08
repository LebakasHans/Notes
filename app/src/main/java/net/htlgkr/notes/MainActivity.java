package net.htlgkr.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String saveFileName = "savedNotes.csv";
    private RecyclerView noteRecyclerView;
    private List<Note> noteList = new ArrayList<>();
    private BroadcastReceiver mReceiver;
    private NotesAdapter mNoteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteRecyclerView = findViewById(R.id.notesRV);
        mNoteAdapter = new NotesAdapter(noteList, this);
        noteRecyclerView.setAdapter(mNoteAdapter);

        setUpReceiver();
    }

    private void setUpReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if((boolean) intent.getSerializableExtra("isEdit")){
                    setNoteInList(
                            (Note) intent.getSerializableExtra("newNote")
                            , intent.getIntExtra("position", -1)
                    );
                }
                else if(!(boolean) intent.getSerializableExtra("isDetail")){
                    Note newNote = (Note) intent.getSerializableExtra("newNote");
                    if (newNote != null) {
                        addNoteToList(newNote);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("New-Note"));
    }

    private void setNoteInList(Note note, int position) {
        noteList.set(position, note);
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void addNoteToList(Note note) {
        noteList.add(note);
        Collections.sort(noteList, (o1, o2) -> o1.getDate().isBefore(o2.getDate()) ? 1 : -1);
        mNoteAdapter.notifyDataSetChanged();
    }

    private void removeNote(Note note) {
        noteList.remove(note);
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId() ;
        switch (id ) {
            case R.id.newNote:
                addNewNote();
                break;
            case R.id.saveNotes:
                saveNotes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNotes() {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(openFileOutput(saveFileName, MODE_PRIVATE)))) {
            out.write(Note.CSV_HEADER);
            out.newLine();
            noteList.stream()
                    .forEach(note -> {
                        try {
                            out.write(Note.serialize(note));
                            out.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewNote() {
        startActivity(new Intent(this, NewOrEditNote.class));
    }


}