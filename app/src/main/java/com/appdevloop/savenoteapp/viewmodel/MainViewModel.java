package com.appdevloop.savenoteapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.appdevloop.savenoteapp.database.AppRepository;
import com.appdevloop.savenoteapp.database.NoteEntity;
import com.appdevloop.savenoteapp.utils.SampleData;

import java.util.List;

/**
 * Created by AppDevloop on 25/07/2018.
 */
public class MainViewModel extends AndroidViewModel {

    public LiveData<List<NoteEntity>> mNotes;
    private AppRepository mRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mNotes = mRepository.mNotes;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAllNotes() {
        mRepository.deleteAllNotes();
    }
}
