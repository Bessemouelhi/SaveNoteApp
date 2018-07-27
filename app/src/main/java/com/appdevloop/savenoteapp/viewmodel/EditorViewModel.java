package com.appdevloop.savenoteapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.appdevloop.savenoteapp.database.AppRepository;
import com.appdevloop.savenoteapp.database.NoteEntity;

import java.util.Date;
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
        NoteEntity liveNote = mLiveNote.getValue();
        if (liveNote != null) {
            liveNote.setText(noteText.trim());
        } else {
            if (TextUtils.isEmpty(noteText.trim())) {
                return;
            }
            liveNote = new NoteEntity(new Date(), noteText.trim());
        }
        mRepository.insertNote(liveNote);
    }

    public void deleteNote() {
        mRepository.deleteNote(mLiveNote.getValue());
    }
}
