package ru.dev2dev.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.dev2dev.notes.data.NotesContract.NoteEntry;

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
        db.execSQL("CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteEntry.COLUMN_TITLE + ", " +
                NoteEntry.COLUMN_DESCRIPTION + ", " +
                NoteEntry.COLUMN_IMAGE_PATH + ", " +
                NoteEntry.COLUMN_DATE +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME);
        onCreate(db);
    }
}
