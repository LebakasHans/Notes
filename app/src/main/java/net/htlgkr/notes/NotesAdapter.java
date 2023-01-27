package net.htlgkr.notes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private List<Note> noteList;
    private int layoutId;
    private LayoutInflater inflater;


    public NotesAdapter(Context context, int listViewItemLayoutId, List<Note> noteList) {
        this.noteList = noteList;
        this.layoutId = listViewItemLayoutId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = noteList.get(position);
        View listItem = (convertView == null) ? inflater.inflate(this.layoutId, null) : convertView;
        ((TextView) listItem.findViewById(R.id.noteLayoutTitle))
                .setText(note.getTitle());
        ((TextView) listItem.findViewById(R.id.noteLayoutNotes))
                .setText(note.getNotes());
        ((TextView) listItem.findViewById(R.id.noteLayoutDate))
                .setText(
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(note.getDate())
                );
        if (note.getDate().isBefore(LocalDateTime.now())){
            setBackgroundColorOfNote(listItem, "#FFCCCB");
        }else {
            setBackgroundColorOfNote(listItem, "#FFFFFF");
        }
        return listItem;
    }

    private void setBackgroundColorOfNote(View listItem, String color) {
        Drawable drawable = ContextCompat.getDrawable(inflater.getContext(), R.drawable.note_item_border);
        GradientDrawable gradientDrawable  = (GradientDrawable) drawable;
        gradientDrawable.setColor(Color.parseColor(color));
        if(listItem != null)
            listItem.setBackground(gradientDrawable);
    }
}