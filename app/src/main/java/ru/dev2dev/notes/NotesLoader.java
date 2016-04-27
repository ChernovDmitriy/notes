package ru.dev2dev.notes;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NotesLoader extends AsyncTaskLoader<ArrayList<Note>> {

    private ArrayList<Note> notes;
    private Context context;

    public NotesLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public ArrayList<Note> loadInBackground() {
        return new NotesDbHelper(context).getNotes();
    }

    @Override
    protected void onStartLoading() {
        if (notes != null) {
            deliverResult(notes);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(ArrayList<Note> data) {
        notes = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

}
