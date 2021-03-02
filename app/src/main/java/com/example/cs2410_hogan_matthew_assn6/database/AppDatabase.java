package com.example.cs2410_hogan_matthew_assn6.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.cs2410_hogan_matthew_assn6.models.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao getContactDao();
}