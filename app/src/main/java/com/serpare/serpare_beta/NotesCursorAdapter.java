package com.serpare.serpare_beta;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.serpare.serpare_beta.database.noteDatabase.DBOpenHelper;

public class NotesCursorAdapter extends CursorAdapter{
    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.note_list_layout,parent,false
        );

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String noteText = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT)
        );

        Integer pos = noteText.indexOf(10);
        if (pos != -1) {
            noteText = noteText.substring(0,pos)+"...";
        }

        TextView tv = (TextView) view.findViewById(R.id.tvNote);
        tv.setText(noteText);

    }
}
