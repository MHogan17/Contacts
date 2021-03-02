package com.example.cs2410_hogan_matthew_assn6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.example.cs2410_hogan_matthew_assn6.database.AppDatabase;
import com.example.cs2410_hogan_matthew_assn6.presenters.BaseMVPView;

public class BaseActivity extends AppCompatActivity implements BaseMVPView {
    @Override
    public AppDatabase getContextDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "contacts").build();
    }
}