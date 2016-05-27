package ru.dev2dev.notes.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.net.Uri;

import ru.dev2dev.notes.Note;

/**
 * Created by dmitriy on 27.05.16.
 */
public class NoteAsyncHandler extends AsyncQueryHandler {

    public NoteAsyncHandler(ContentResolver cr) {
        super(cr);
    }

    public void update(Note note) {
        Uri uri = NotesContract.NoteEntry.buildNoteUri(note.getId());
        startUpdate(0, null, uri, note.getContentValues(), null, null);
    }

    public void insert(Note note) {
        Uri uri = NotesContract.NoteEntry.buildNotesUri();
        startInsert(0, null, uri, note.getContentValues());
    }

    public void delete(Note note) {
        Uri uri = NotesContract.NoteEntry.buildNoteUri(note.getId());
        startDelete(0, null, uri, null, null);
    }
}
