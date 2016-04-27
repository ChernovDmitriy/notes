package ru.dev2dev.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.dev2dev.notes.data.NotesDbHelper;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NoteListFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerView;

    private static final int NOTES_LOADER_ID = 1;

    NotesDbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(NOTES_LOADER_ID, null, new NotesLoaderCallbacks());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new NotesDbHelper(getActivity());

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
                dbHelper.deleteNotes();
                Snackbar.make(v, "delete all notes", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void updateUI(ArrayList<Note> notes) {

        NoteAdapter noteAdapter = new NoteAdapter(notes);
        noteAdapter.setCardViewClickListener(new NoteAdapter.onCardViewClickListener() {
            @Override
            public void onCardViewClick(Note note) {
                openDialog(note);
            }
        });

        recyclerView.setAdapter(noteAdapter);

//        getLoaderManager().destroyLoader(NOTES_LOADER_ID);
    }

    private class NotesLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Note>> {

        @Override
        public Loader<ArrayList<Note>> onCreateLoader(int id, Bundle args) {
            return new NotesLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Note>> loader, ArrayList<Note> notes) {
            updateUI(notes);
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Note>> loader) {

        }
    }

    private void openDialog(Note note) {
        Log.d(TAG, "openDialog: note is "+note);
        Bundle bundle = new Bundle();
        bundle.putSerializable(NoteEditFragment.NOTE_EXTRA, note);

        NoteEditFragment fragment = new NoteEditFragment();
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), "note");
    }
}
