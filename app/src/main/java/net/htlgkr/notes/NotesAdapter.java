package net.htlgkr.notes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private Context context;

    public NotesAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notelayout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = noteList.get(position);
        holder.titleTextView.setText(currentNote.getTitle());
        holder.notesTextView.setText(currentNote.getNotes());
        holder.dateTextView.setText(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(currentNote.getDate())
        );

        if (currentNote.getDate().isBefore(LocalDateTime.now())){
            setBackgroundColorOfNote(holder.itemView, "#FFCCCB");
        }else {
            setBackgroundColorOfNote(holder.itemView, "#FFFFFF");
        }
    }

    private void setBackgroundColorOfNote(View listItem, String color) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.note_item_border);
        GradientDrawable gradientDrawable  = (GradientDrawable) drawable;
        gradientDrawable.setColor(Color.parseColor(color));
        if(listItem != null)
            listItem.setBackground(gradientDrawable);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, notesTextView, dateTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteLayoutTitle);
            notesTextView = itemView.findViewById(R.id.noteLayoutNotes);
            dateTextView = itemView.findViewById(R.id.noteLayoutDate);
        }
    }
}
