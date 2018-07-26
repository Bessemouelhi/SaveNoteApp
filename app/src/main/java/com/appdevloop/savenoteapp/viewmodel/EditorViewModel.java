package com.appdevloop.savenoteapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.appdevloop.savenoteapp.database.AppRepository;
import com.appdevloop.savenoteapp.database.NoteEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by AppDevloop on 26/07/2018.
 */
public class EditorViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    public MutableLiveData<NoteEntity> mLiveNote = new MutableLiveData<>();
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void loadData(final int noteId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity note = mRepository.getNoteById(noteId);
                mLiveNote.postValue(note);
            }
        });
    }

    public void saveNote(String noteText) {
        NoteEntity noteEntity = mLiveNote.getValue();
        if (noteEntity != null) {
            noteEntity.setText(noteText);
        }
        mRepository.insertNote(noteEntity);
    }
}
