package com.appdevloop.savenoteapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.appdevloop.savenoteapp.database.NoteEntity;
import com.appdevloop.savenoteapp.utils.Constants;
import com.appdevloop.savenoteapp.utils.StorageUtils;
import com.appdevloop.savenoteapp.viewmodel.EditorViewModel;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import icepick.Icepick;
import icepick.State;

public class EditorActivity extends AppCompatActivity {

    // FILE PURPOSE
    private static final String FILENAME = "saveNoteApp.txt";
    private static final String FOLDERNAME = "SaveNoteApp";
    // PERMISSION PURPOSE
    private static final int RC_STORAGE_WRITE_PERMS = 100;
    // Define the authority of the FileProvider
    private static final String AUTHORITY = "com.appdevloop.savenoteapp.fileprovider";

    private EditorViewModel mViewModel;
    private boolean isNewNote;
    @State boolean isEditing;

    @BindView(R.id.et_note_text)
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Restore all @State annotation variables in Bundle
        Icepick.restoreInstanceState(this, savedInstanceState);

        initViewModel();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isEditing = true;
        // Save all @State annotation variables in Bundle
        Icepick.saveInstanceState(this, outState);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);
        mViewModel.mLiveNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                if (noteEntity != null && !isEditing) {
                    mEditText.setText(noteEntity.getText());
                }
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

    // 2 - Share the internal file
    private void shareFile(){
        if (!mEditText.getText().equals("")) {
            StorageUtils.setTextInStorage(getFilesDir(), this, FILENAME, FOLDERNAME, this.mEditText.getText().toString());
            File internalFile = StorageUtils.getFileFromStorage(getFilesDir(), this, FILENAME, FOLDERNAME);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), AUTHORITY, internalFile);

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/*");
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.trip_book_share)));
        }

    }

    @OnClick(R.id.fab_editor)
    void onFabClick(View view) {
        shareFile();
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
