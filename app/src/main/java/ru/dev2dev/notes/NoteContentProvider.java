package ru.dev2dev.notes;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NoteContentProvider extends ContentProvider {

    final String TAG = this.getClass().getSimpleName();

    static final String DB_NAME = "noteDB";
    static final int DB_VERSION = 1;

    static final String DB_CREATE = "CREATE TABLE "+Note.NOTE+"("+
            Note.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            Note.TITLE+", "+
            Note.DESCRIPTION+", "+
            Note.IMAGE_PATH+", "+
            Note.DATE+
            ")";

    static final String AUTHORITY = "ru.dev2dev.notes.provider";

    static final String NOTE_PATH = "notes";

    public static final Uri NOTE_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + NOTE_PATH);

    static final String NOTE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + NOTE_PATH;

    static final String NOTE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + NOTE_PATH;

    static final int URI_NOTES = 1;

    static final int URI_NOTES_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, URI_NOTES);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH + "/#", URI_NOTES_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                Log.d(TAG, "URI_NOTES");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Note.TITLE + " ASC";
                }
                break;
            case URI_NOTES_ID:
                String id = uri.getLastPathSegment();
                Log.d(TAG, "URI_NOTES_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = Note.ID + " = " + id;
                } else {
                    selection = selection + " AND " + Note.ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(Note.NOTE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                NOTE_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_NOTES)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(Note.NOTE, null, values);
        Uri resultUri = ContentUris.withAppendedId(NOTE_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                Log.d(TAG, "URI_NOTES");
                break;
            case URI_NOTES_ID:
                String id = uri.getLastPathSegment();
                Log.d(TAG, "URI_NOTES_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = Note.ID + " = " + id;
                } else {
                    selection = selection + " AND " + Note.ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(Note.NOTE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                Log.d(TAG, "URI_NOTES");

                break;
            case URI_NOTES_ID:
                String id = uri.getLastPathSegment();
                Log.d(TAG, "URI_NOTES_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = Note.ID + " = " + id;
                } else {
                    selection = selection + " AND " + Note.ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(Note.NOTE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                return NOTE_CONTENT_TYPE;
            case URI_NOTES_ID:
                return NOTE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

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

}
