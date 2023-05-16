package com.example.itubeapp.persistent;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.itubeapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository implements RepositoryFactory<User> {
    private final SQLiteDatabase database;
    private final String repositoryName;

    public UserRepository(SQLiteDatabase database, String repositoryName) {
        this.database = database;
        this.repositoryName = repositoryName;
        onCreate();
    }

    private void createTableIfNotExists() {
        // Create the "users" table if it doesn't exist
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, hash_password TEXT)";
        database.execSQL(createTableQuery);
    }

    @Override
    public void onCreate() {
        createTableIfNotExists();
    }

    @Override
    public void drop() {
        String dropTableQuery = "DROP TABLE IF EXISTS users";
        database.execSQL(dropTableQuery);
    }

    @Override
    public void create(User user) {
        ContentValues values = new ContentValues();
        values.put("hash_password", user.getPassword());
        values.put("name", user.getName());

        database.insert("users", null, values);
    }

    @Override
    public void delete(User user) {
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(user.getId())};

        database.delete("users", whereClause, whereArgs);
    }

    @Override
    public void update(User user) {
        ContentValues values = new ContentValues();
        values.put("name", user.getName());

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(user.getId())};

        database.update("users", values, whereClause, whereArgs);
    }

    @Override
    public User read(int id) {
        String[] columns = {"id", "name", "hash_password"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return readUserFromDatabase(columns, selection, selectionArgs);
    }

    @Override
    public User read(String name) {
        String[] columns = {"id", "name", "hash_password"};
        String selection = "name = ?";
        String[] selectionArgs = {name};

        return readUserFromDatabase(columns, selection, selectionArgs);
    }

    @Override
    public String getRepositoryName() {
        return repositoryName;
    }

    private User mapCursorToUser(Cursor cursor) {
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String hashPassword = cursor.getString(cursor.getColumnIndexOrThrow("hash_password"));

        return new User(userId, name, hashPassword);
    }

    private User readUserFromDatabase(String[] columns, String selection, String[] selectionArgs) {
        Cursor cursor = database.query("users", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            User user = mapCursorToUser(cursor);
            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }

    @Override
    public List<User> readAll() {
        List<User> userList = new ArrayList<>();

        String[] columns = {"id", "name", "hash_password"};

        Cursor cursor = database.query("users", columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            userList.add(mapCursorToUser(cursor));
        }

        cursor.close();
        return userList;
    }
}
