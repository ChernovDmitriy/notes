package ru.dev2dev.notes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import ru.dev2dev.notes.data.NotesContract.NoteEntry;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NotesProvider extends ContentProvider {
    final String TAG = this.getClass().getSimpleName();

    static final int NOTES = 100;
    static final int NOTE = 101;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES, NOTES);
        uriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES + "/#", NOTE);
    }

    private NotesDbHelper dbHelper;

    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        dbHelper = new NotesDbHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case NOTES:
                Log.d(TAG, "NOTES");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NoteEntry._ID + " ASC";
                }
                break;

            case NOTE:
                String id = uri.getLastPathSegment();
                Log.d(TAG, "NOTE, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = NoteEntry._ID + " = ?";
                } else {
                    selection = selection + " AND " +  NoteEntry._ID + " = ?";
                }
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        Cursor cursor = dbHelper.getReadableDatabase().query(
                NoteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert, " + uri.toString());
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri resultUri;

        switch (uriMatcher.match(uri)) {
            case NOTES:
                long noteID = db.insert(NoteEntry.TABLE_NAME, null, values);
                if (noteID > 0) {
                    resultUri = NoteEntry.buildNoteUri(noteID);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete, " + uri.toString());
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case NOTES:
                Log.d(TAG, "NOTES");
                break;

            case NOTE:
                String id = uri.getLastPathSegment();
                Log.d(TAG, "NOTE, " + id);

                if (TextUtils.isEmpty(selection)) {
                    selection = NoteEntry._ID + " = ?";
                } else {
                    selection = selection + " AND " + NoteEntry._ID + " = ?";
                }
                selectionArgs = new String[] {id};
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        int deletedRows = db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs);
        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case NOTES:
                Log.d(TAG, "NOTES");
                break;

            case NOTE:
                String id = uri.getLastPathSegment();
                Log.d(TAG, "NOTE, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = NoteEntry._ID + " = ?";
                } else {
                    selection = selection + " AND " + NoteEntry._ID + " = ?";
                }
                selectionArgs = new String[] {id};
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        int updatedRows = db.update(NoteEntry.TABLE_NAME, values, selection, selectionArgs);
        if (updatedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRows;
    }

    public String getType(Uri uri) {
        Log.d(TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case NOTES:
                return NoteEntry.CONTENT_TYPE;
            case NOTE:
                return NoteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case NOTES:
                db.beginTransaction();
                int count = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(NoteEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            count++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
