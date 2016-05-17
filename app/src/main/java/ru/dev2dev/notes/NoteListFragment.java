package ru.dev2dev.notes;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.dev2dev.notes.adapters.NoteAdapter;
import ru.dev2dev.notes.data.NotesContract.NoteEntry;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NoteListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTES_LOADER_ID = 1;

    private RecyclerView recyclerView;
    private NoteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(null);
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Uri uri = NoteEntry.buildNotesUri();
                getActivity().getContentResolver().delete(uri, null, null);
                Snackbar.make(v, "delete all notes", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new NoteAdapter(new NoteAdapter.OnCardViewClickListener() {
            @Override
            public void onCardViewClick(Note note) {
                openDialog(note);
            }
        });
        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(NOTES_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case NOTES_LOADER_ID:
                Uri notesUri = NoteEntry.buildNotesUri();
                String[] projection = {NoteEntry._ID,
                        NoteEntry.COLUMN_TITLE,
                        NoteEntry.COLUMN_DESCRIPTION,
                        NoteEntry.COLUMN_IMAGE_PATH,
                        NoteEntry.COLUMN_DATE};
                return new CursorLoader(getContext(),
                        notesUri,
                        projection,
                        null,
                        null,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case NOTES_LOADER_ID:
                adapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case NOTES_LOADER_ID:
                adapter.swapCursor(null);
                break;
        }
    }

    private void openDialog(Note note) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(NoteEditFragment.NOTE_EXTRA, note);

        NoteEditFragment fragment = new NoteEditFragment();
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), "note");
    }
}
