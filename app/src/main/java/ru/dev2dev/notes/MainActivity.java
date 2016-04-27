package ru.dev2dev.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment noteListFragment = fm.findFragmentById(R.id.container);
        if (noteListFragment==null) {
            noteListFragment = new NoteListFragment();
            fm.beginTransaction()
                    .add(R.id.container, noteListFragment)
                    .commit();
        }
    }

}
