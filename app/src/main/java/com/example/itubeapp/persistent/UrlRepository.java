package com.example.itubeapp.persistent;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.itubeapp.handlers.ServicesHandler;
import com.example.itubeapp.models.Url;
import com.example.itubeapp.models.User;
import com.example.itubeapp.services.authenticate.session.SessionService;

import java.util.ArrayList;
import java.util.List;

public class UrlRepository implements RepositoryFactory<Url> {
    private final SQLiteDatabase database;
    private final String repositoryName;

    public UrlRepository(SQLiteDatabase database, String repositoryName) {
        this.database = database;
        this.repositoryName = repositoryName;
        onCreate();
    }

    private void createTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS urls (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, url TEXT, FOREIGN KEY(user_id) REFERENCES users(id))";

        database.execSQL(createTableQuery);
    }

    private Url mapCursorToUrl(Cursor cursor) {
        int urlId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));

        return new Url(urlId, url, userId);
    }

    private Url readUrlFromDatabase(String[] columns, String selection, String[] selectionArgs) {
        Cursor cursor = database.query("urls", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            Url url = mapCursorToUrl(cursor);
            cursor.close();
            return url;
        }

        cursor.close();
        return null;
    }

    @Override
    public void create(Url url) {
        // insert url to database
        ContentValues values = new ContentValues();
        values.put("user_id", url.getUserId());
        values.put("url", url.getUrl());

        database.insert("urls", null, values);

    }

    @Override
    public void delete(Url url) {
        // not required for this task
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(url.getId())};

        database.delete("urls", whereClause, whereArgs);
    }

    @Override
    public void update(Url repository) {
        // not required for this task
    }

    /**
     * Read all urls from the database that match the current user
     *
     * @return List of urls that match the current user
     */
    @Override
    public List<Url> readAll() {
        List<Url> urlList = new ArrayList<>();

        String[] columns = {"id", "user_id", "url"};
        String selection = "user_id = ?";

        // get current user details from session
        SessionService sessionService = (SessionService) ServicesHandler.getInstance().getService(SessionService.class.getName());
        User user = sessionService.getSession();
        String[] selectionArgs = {String.valueOf(user.getId())};

        Cursor cursor = database.query("urls", columns, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            urlList.add(mapCursorToUrl(cursor));
        }

        cursor.close();
        return urlList;
    }

    @Override
    public Url read(int id) {
        String[] columns = {"id", "user_id", "url"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return readUrlFromDatabase(columns, selection, selectionArgs);
    }

    @Override
    public Url read(String name) {
        String[] columns = {"id", "user_id", "url"};
        String selection = "url = ?";
        String[] selectionArgs = {name};

        return readUrlFromDatabase(columns, selection, selectionArgs);
    }

    @Override
    public String getRepositoryName() {
        return repositoryName;
    }

    @Override
    public void onCreate() {
        createTableIfNotExists();
    }

    @Override
    public void drop() {
        String dropTableQuery = "DROP TABLE IF EXISTS urls";
        database.execSQL(dropTableQuery);
    }
}
