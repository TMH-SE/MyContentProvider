package com.example.mycontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "StudentDB";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String scriptStudent = "CREATE TABLE Student(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100) NOT NULL, class_name VARCHAR(100) NOT NULL)";
        db.execSQL(scriptStudent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
