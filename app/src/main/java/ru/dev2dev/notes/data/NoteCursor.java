package ru.dev2dev.notes.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.dev2dev.notes.Note;

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
        long id = getLong(getColumnIndex(Note.ID));
        String title = getString(getColumnIndex(Note.TITLE));
        String description = getString(getColumnIndex(Note.DESCRIPTION));
        String imagePath = getString(getColumnIndex(Note.IMAGE_PATH));
        String date = getString(getColumnIndex(Note.DATE));

        return new Note(id, title, description, imagePath, date);
    }
}
