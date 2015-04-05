package ru.tech_mail.evolution.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import static ru.tech_mail.evolution.content_provider.EvolutionDatabaseContract.*;

/**
 * Created by Ivan on 04.04.2015.
 */
public class EvolutionContentProvider extends ContentProvider {
    private static final UriMatcher URI_MATCHER;

    private EvolutionDatabaseHelper helper;


    private static final int TECHNOLOGIES = 0;
    private static final int TECHNOLOGIES_ID = 1;


    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, Technologies.TABLE_NAME, TECHNOLOGIES);
        URI_MATCHER.addURI(AUTHORITY, Technologies.TABLE_NAME + "/#", TECHNOLOGIES_ID);
    }
    @Override
    public boolean onCreate() {
        helper = new EvolutionDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase dbConnection = helper.getReadableDatabase();
        Cursor cursor;
        switch (URI_MATCHER.match(uri)) {
            case TECHNOLOGIES:
                cursor = dbConnection.query(Technologies.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TECHNOLOGIES_ID:
                cursor = dbConnection.query(Technologies.TABLE_NAME, projection, "_ID=?", new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            default:
                throw new IllegalStateException();
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TECHNOLOGIES:
                return Technologies.CONTENT_TYPE;
            case TECHNOLOGIES_ID:
                return Technologies.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri result;
        long id;
        final SQLiteDatabase dbConnection = helper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case TECHNOLOGIES:
                id = dbConnection.insertWithOnConflict(Technologies.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                result = ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalStateException();
        }
        return result;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException();
    }



    private final class EvolutionDatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "evolution.db";
        private static final int DATABASE_VERSION = 1;

        public EvolutionDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(Technologies.SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL(Technologies.SQL_DROP);
            sqLiteDatabase.execSQL(Technologies.SQL_CREATE);
            onCreate(sqLiteDatabase);
        }
    }
}
