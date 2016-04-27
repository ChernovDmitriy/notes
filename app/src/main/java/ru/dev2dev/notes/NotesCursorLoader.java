package ru.dev2dev.notes;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import ru.dev2dev.notes.data.NoteContentProvider;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NotesCursorLoader extends CursorLoader {

    public NotesCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = getContext().getContentResolver().query(
                NoteContentProvider.NOTE_CONTENT_URI,
                null,null,null,null
        );
        return super.loadInBackground();
    }
}
