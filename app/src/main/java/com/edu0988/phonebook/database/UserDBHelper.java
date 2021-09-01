package com.edu0988.phonebook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "userbase.db";

    private static UserDBHelper sInstance;

    public static synchronized void init(Context context) {
        if (sInstance == null) {
            sInstance = new UserDBHelper(context.getApplicationContext());
        }
    }

    public static synchronized UserDBHelper get() {
        return sInstance;
    }

    private UserDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserDBSchema.UserTable.NAME + " (" +
                "_id integer primary key autoincrement, " +
                UserDBSchema.Cols.UUID + ", " +
                UserDBSchema.Cols.USERNAME + ", " +
                UserDBSchema.Cols.USERLASTNAME + ", " +
                UserDBSchema.Cols.PHONE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists " + UserDBSchema.UserTable.NAME);
            onCreate(db);
        }
    }
}
