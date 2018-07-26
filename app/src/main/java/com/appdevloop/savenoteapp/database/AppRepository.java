package com.appdevloop.savenoteapp.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.appdevloop.savenoteapp.utils.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by AppDevloop on 25/07/2018.
 */
public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<NoteEntity>> mNotes;
    private AppDatabase mDb;
    // One Executor for all operation (queued one after another)
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mNotes = getAllNotes();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDao().insertAll(SampleData.getNotes());
            }
        });
    }

    private LiveData<List<NoteEntity>> getAllNotes() {
        return mDb.noteDao().getAll();
    }

    public void deleteAllNotes() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.noteDao().deleteAll();
            }
        });
    }
}
