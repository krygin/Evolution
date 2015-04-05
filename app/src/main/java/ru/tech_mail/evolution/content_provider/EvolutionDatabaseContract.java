package ru.tech_mail.evolution.content_provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ivan on 04.04.2015.
 */
public final class EvolutionDatabaseContract {
    public static final String AUTHORITY = "ru.tech_mail.evolution.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);


    private EvolutionDatabaseContract () {

    }


    public static final class Technologies implements BaseColumns {
        public static final String TABLE_NAME = "technologies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PICTURE = "picture";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_INFO = "info";

        public static final String [] ALL_COLUMNS_PROJECTION = {_ID, COLUMN_ID, COLUMN_PICTURE, COLUMN_TITLE, COLUMN_INFO};
        public static final String[] ID_COLUMN_PROJECTION = {_ID};

        static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                COLUMN_ID + " INTEGER UNIQUE" + "," +
                COLUMN_PICTURE + " TEXT" + "," +
                COLUMN_TITLE + " TEXT" + "," +
                COLUMN_INFO + " TEXT" + ");";
        static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TABLE_NAME);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.evolution.technologies";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.evolution.technologies";
    }
}
