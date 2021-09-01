package com.edu0988.phonebook.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu0988.phonebook.model.User;
import com.edu0988.phonebook.database.UserDBHelper;
import com.edu0988.phonebook.database.UserDBSchema;

import java.util.ArrayList;
import java.util.List;

public class UserAPISqlLite implements UserAPI {

    private final SQLiteDatabase db;

    public UserAPISqlLite(Context context) {
        UserDBHelper.init(context);
        db = UserDBHelper.get().getWritableDatabase();
    }

    @Override
    public boolean add(User user) {
        db.insert(UserDBSchema.UserTable.NAME, null, getContentValues(user));
        return true;
    }

    @Override
    public User get(String uuid) {
        List<User> userList = getAll();
        for (User user : userList) {
            if (user.getUuid().equals(uuid)) return user;
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        Cursor cursor = queryAll();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            userList.add(get(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    @Override
    public boolean update(User updateUser) {
        User user = get(updateUser.getUuid());
        if (user == null) {
            return add(updateUser);
        } else {
            db.update(
                    UserDBSchema.UserTable.NAME,
                    getContentValues(updateUser),
                    "uuid = ?",
                    new String[]{user.getUuid()}
            );
            return true;
        }
    }

    @Override
    public boolean delete(String uuid) {
        User user = get(uuid);
        if (user != null) {
            db.delete(
                    UserDBSchema.UserTable.NAME,
                    "uuid = ?",
                    new String[]{uuid}
            );
            return true;
        }
        return false;
    }

    private Cursor queryAll() {
        return db.query(
                UserDBSchema.UserTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserDBSchema.Cols.UUID, user.getUuid());
        values.put(UserDBSchema.Cols.USERNAME, user.getName());
        values.put(UserDBSchema.Cols.USERLASTNAME, user.getLastname());
        values.put(UserDBSchema.Cols.PHONE, user.getPhone());
        return values;
    }

    private static User get(Cursor cursor) {
        String uuidString = cursor.getString(cursor.getColumnIndex(UserDBSchema.Cols.UUID));
        String userName = cursor.getString(cursor.getColumnIndex(UserDBSchema.Cols.USERNAME));
        String userLastName = cursor.getString(cursor.getColumnIndex(UserDBSchema.Cols.USERLASTNAME));
        String phone = cursor.getString(cursor.getColumnIndex(UserDBSchema.Cols.PHONE));
        User user = new User(uuidString);
        user.setName(userName);
        user.setLastname(userLastName);
        user.setPhone(phone);
        return user;
    }
}
