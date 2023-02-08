package net.htlgkr.notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private int position;

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
        setPosition(position);
        holder.itemView.setTag(position);

        Note currentNote = noteList.get(position);
        holder.titleTextView.setText(currentNote.getTitle());
        holder.notesTextView.setText(currentNote.getNotes());
        holder.dateTextView.setText(
                DateTimeFormatter.ofPattern(Note.DATE_PATTERN).format(currentNote.getDate())
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

    public void setPosition(int position) {
        this.position = position;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener{
        TextView titleTextView, notesTextView, dateTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteLayoutTitle);
            notesTextView = itemView.findViewById(R.id.noteLayoutNotes);
            dateTextView = itemView.findViewById(R.id.noteLayoutDate);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem details = menu.add(Menu.NONE, 2, 2, "Details");
            MenuItem delete = menu.add(Menu.NONE, 3, 3, "Delete");

            edit.setOnMenuItemClickListener(onEditMenu);
            details.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent intent = new Intent(context, NewOrEditNote.class);
                switch (item.getItemId()) {
                    case 1:
                        intent.putExtra("EDIT", noteList.get(position));
                        intent.putExtra("POSITION", position);
                        context.startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("DETAILS", noteList.get(position));
                        context.startActivity(intent);
                        break;
                    case 3:
                        noteList.remove(noteList.get(position));
                        notifyDataSetChanged();
                        break;
                }
                return true;
            }
        };
    }
}
