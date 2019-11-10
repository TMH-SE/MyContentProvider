package com.example.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.mycontentprovider.MyContentProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/students";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final int STUDENTS = 1;
    static final int STUDENT_ID = 2;
    static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PROVIDER_NAME, "students", STUDENTS);
        URI_MATCHER.addURI(PROVIDER_NAME, "students/#", STUDENT_ID);
    }

    private SQLiteDatabase db;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case STUDENTS:
                return db.delete("Student", selection, selectionArgs);
             default:
                 throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert("Student", null, values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        db = helper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("Student");
        switch (URI_MATCHER.match(uri)) {
            case STUDENT_ID:
                builder.appendWhere("id = " + uri.getPathSegments().get(1));
                break;
            default:
                break;
        }
        Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = db.update("Student", values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
