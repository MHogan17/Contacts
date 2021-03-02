package com.example.cs2410_hogan_matthew_assn6.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first")
    public String first;

    @ColumnInfo(name = "last")
    public String last;

    @ColumnInfo(name = "number")
    public String number;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "picture_uri")
    public String pictureUri;

}
