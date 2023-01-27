package net.htlgkr.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView noteListView;
    private NotesAdapter mNotesAdapter;
    private List<Note> noteList = new ArrayList<>();
    private BroadcastReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteListView = findViewById(R.id.notesLV);
        mNotesAdapter = new NotesAdapter(this, R.layout.notelayout, noteList);
        noteListView.setAdapter(mNotesAdapter);
        setUpReceiver();
    }

    private void setUpReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Note newNote = (Note) intent.getSerializableExtra("newNote");
                if (newNote != null) {
                    addNoteToList(newNote);
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("New-Note"));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void addNoteToList(Note note) {
        noteList.add(note);
        noteListView.setAdapter(mNotesAdapter);
        mNotesAdapter.notifyDataSetChanged();
    }

    private void removeNote(Note note) {
        noteList.remove(note);
        noteListView.setAdapter(mNotesAdapter);
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
                //TODO
                Toast.makeText(this, "Help Clicked", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewNote() {
        startActivity(new Intent(this, NewOrEditNote.class));
    }


}