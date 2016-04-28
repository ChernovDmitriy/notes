package ru.dev2dev.notes.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.dev2dev.notes.Note;
import ru.dev2dev.notes.data.NotesContract.NoteEntry;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NoteCursor extends CursorWrapper {

    public NoteCursor(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        if (isBeforeFirst() || isAfterLast()) {
            return null;
        }
        long id = getLong(getColumnIndex(NoteEntry._ID));
        String title = getString(getColumnIndex(NoteEntry.COLUMN_TITLE));
        String description = getString(getColumnIndex(NoteEntry.COLUMN_DESCRIPTION));
        String imagePath = getString(getColumnIndex(NoteEntry.COLUMN_IMAGE_PATH));
        String date = getString(getColumnIndex(NoteEntry.COLUMN_DATE));

        return new Note(id, title, description, imagePath, date);
    }
}
