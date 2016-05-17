package ru.dev2dev.notes;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.dev2dev.notes.data.NotesContract.NoteEntry;

public class NoteEditFragment extends DialogFragment {
    public static final String NOTE_EXTRA = "ru.dev2dev.notes.note_extra";

    private Note note;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        note = (Note) getArguments().getSerializable(NOTE_EXTRA);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_note_edit, null);
        final EditText titleText = (EditText) view.findViewById(R.id.titleText);
        final EditText descText = (EditText) view.findViewById(R.id.descText);

        titleText.setText(note.getTitle());
        descText.setText(note.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.note_editing)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //update note
                        note.setTitle(titleText.getText().toString());
                        note.setDescription(descText.getText().toString());

                        //save it to DB
                        save(note);

                        dismiss();
                    }
                });

        //add delete button if note exists
        if (note.getId()!=0) {
            builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = NoteEntry.buildNoteUri(note.getId());
                    getActivity().getContentResolver().delete(uri, null, null);
                }
            });
        }

        return builder.create();
    }

    private void save(Note note) {
        ContentValues noteValues = new ContentValues();
        noteValues.put(NoteEntry.COLUMN_TITLE, note.getTitle());
        noteValues.put(NoteEntry.COLUMN_DESCRIPTION, note.getDescription());

        long id = note.getId();
        if (id != 0) {
            Uri uri = NoteEntry.buildNoteUri(id);
            getActivity().getContentResolver().update(uri, noteValues, null, null);
        } else {
            Uri uri = NoteEntry.buildNotesUri();
            getActivity().getContentResolver().insert(uri, noteValues);
        }
    }
}
