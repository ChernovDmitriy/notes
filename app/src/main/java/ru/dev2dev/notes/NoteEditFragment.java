package ru.dev2dev.notes;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.dev2dev.notes.data.NotesDbHelper;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class NoteEditFragment extends DialogFragment {

    public static final String NOTE_EXTRA = "ru.dev2dev.notes.note_extra";

    private Note note;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d("mytag", "onCreateDialog: note is "+getArguments().getSerializable(NOTE_EXTRA));
        if (getArguments().getSerializable(NOTE_EXTRA)!=null) {
            note = (Note) getArguments().getSerializable(NOTE_EXTRA);
        } else {
            note = new Note();
        }

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_note_edit, null);
        final EditText titleText = (EditText) view.findViewById(R.id.titleText);
        final EditText descText = (EditText) view.findViewById(R.id.descText);
        Button button = (Button) view.findViewById(R.id.load_image_button);

        titleText.setText(note.getTitle());
        descText.setText(note.getDescription());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.note_editing)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //update note
                        note.setTitle(titleText.getText().toString());
                        note.setDescription(descText.getText().toString());

                        //save it to DB
                        NotesDbHelper dbHelper = new NotesDbHelper(getActivity());
                        if (note.getId()!=0) {
                            dbHelper.updateNote(note);
                        } else {
                            dbHelper.insertNote(note);
                        }

                        //return updated note
                        Intent intent = new Intent();
                        intent.putExtra(NOTE_EXTRA, note);
                        onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                        dismiss();
                    }
                })
                .create();
    }
}
