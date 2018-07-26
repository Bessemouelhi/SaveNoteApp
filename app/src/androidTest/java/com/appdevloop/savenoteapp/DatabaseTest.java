package com.appdevloop.savenoteapp;

/**
 * Created by AppDevloop on 25/07/2018.
 */
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.provider.LiveFolders;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.appdevloop.savenoteapp.database.AppDatabase;
import com.appdevloop.savenoteapp.database.NoteDao;
import com.appdevloop.savenoteapp.database.NoteEntity;
import com.appdevloop.savenoteapp.utils.SampleData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    public static final String TAG = "Junit";
    private AppDatabase mDb;
    private NoteDao mDao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context,
                                           AppDatabase.class).build();
        mDao = mDb.noteDao();
        Log.i(TAG, "createDb");
    }

    @After
    public void closeDb() {
        mDb.close();
        Log.i(TAG, "closeDb");
    }

    @Test
    public void createAndRetrieveNotes() {
        mDao.insertAll(SampleData.getNotes());
        int count = mDao.getCount();
        Log.i(TAG, "createAndRetrieveNotes: count=" + count);
        assertEquals(SampleData.getNotes().size(), count);
    }

    @Test
    public void deleteAllTest() {
        mDao.insertAll(SampleData.getNotes());
        int count = mDao.getCount();
        mDao.deleteAll();
        Log.i(TAG, "createAndRetrieveNotes: count=" + count);
        assertNotEquals(mDao.getCount(), count);
        assertEquals(0, mDao.getCount());
    }

    @Test
    public void compareStrings() {
        mDao.insertAll(SampleData.getNotes());
        NoteEntity original = SampleData.getNotes().get(0);
        NoteEntity fromDb = mDao.getNoteById(1);
        assertEquals(original.getText(), fromDb.getText());
        assertEquals(1, fromDb.getId());
    }
}