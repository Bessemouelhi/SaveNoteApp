package com.appdevloop.savenoteapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.appdevloop.savenoteapp.database.NoteEntity;
import com.appdevloop.savenoteapp.utils.Constants;
import com.appdevloop.savenoteapp.viewmodel.EditorViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditorActivity extends AppCompatActivity {

    private EditorViewModel mViewModel;
    private boolean isNewNote;

    @BindView(R.id.et_note_text)
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViewModel();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);
        mViewModel.mLiveNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                assert noteEntity != null;
                mEditText.setText(noteEntity.getText());
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New note");
            isNewNote = true;
        } else {
            setTitle("Edit note");
            int noteId = extras.getInt(Constants.KEY_NOTE_ID);
            mViewModel.loadData(noteId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!isNewNote) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                saveAndReturn();
                return true;
            case R.id.action_delete_note:
                deleteNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
        mViewModel.deleteNote();
        finish();
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        mViewModel.saveNote(mEditText.getText().toString());
        finish();
    }

    @OnClick(R.id.fab_editor)
    void onFabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
