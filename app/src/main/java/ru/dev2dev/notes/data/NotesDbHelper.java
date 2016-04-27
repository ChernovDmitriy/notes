package ru.dev2dev.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import ru.dev2dev.notes.Note;

/**
 * Created by Dmitriy on 21.04.2016.
 */
public class NotesDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notesAppDB";
    private static final int DB_VERSION = 1;


    public NotesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ Note.NOTE+"("+
                Note.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Note.TITLE+", "+
                Note.DESCRIPTION+", "+
                Note.IMAGE_PATH+", "+
                Note.DATE+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private ContentValues getContentValues(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(Note.TITLE, note.getTitle());
        cv.put(Note.DESCRIPTION, note.getDescription());
        cv.put(Note.IMAGE_PATH, note.getImagePath());
        return cv;
    }

    public void insertNote(Note note) {
        ContentValues cv = getContentValues(note);
        getWritableDatabase().insert(Note.NOTE, null, cv);
    }

    public void updateNote(Note note) {
        String id = String.valueOf(note.getId());
        ContentValues cv = getContentValues(note);
        getWritableDatabase().update(Note.NOTE, cv, Note.ID+" = ?", new String[]{id});
    }

    public void deleteNote(Note note) {
        String id = String.valueOf(note.getId());
        ContentValues cv = getContentValues(note);
        getWritableDatabase().delete(Note.NOTE, Note.ID+" = ?", new String[]{id});
    }

    public void deleteNotes() {
        getWritableDatabase().delete(Note.NOTE, null, null);
    }

    private NoteCursor queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = getReadableDatabase().query(
                Note.NOTE,//table name
                null,//all columns
                whereClause,
                whereArgs,
                null,//groupBy
                null,//having
                null//orderBy
        );
        return new NoteCursor(cursor);
    }

    private NoteCursor queryNote(long id) {
        String whereClause = Note.ID+" = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return queryNotes(whereClause, whereArgs);
    }

    public ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        NoteCursor noteCursor = queryNotes(null, null);

        try {
            noteCursor.moveToFirst();
            while (!noteCursor.isAfterLast()) {
                notes.add(noteCursor.getNote());
                noteCursor.moveToNext();
            }
        } finally {
            noteCursor.close();
        }
        return notes;
    }

    @Nullable
    public Note getNote(long id) {
        NoteCursor noteCursor = queryNote(id);

        try {
            if (noteCursor.getCount()==0) {
                return null;
            }
            noteCursor.moveToFirst();
            return noteCursor.getNote();
        } finally {
            noteCursor.close();
        }

    }

}
