package org.geekhub.andrewsalive.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ArticlesTable {

    public static final String TABLE_ARTICLES = "articles";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_NICKNAME = "nickname";   
    public static final String COLUMN_TWEETTEXT = "tweettext";

    public static final String[] PROJECTION = {
            BaseColumns._ID,
            COLUMN_ID,
            COLUMN_AVATAR,
            COLUMN_NICKNAME,
            COLUMN_TWEETTEXT,
    };

    private static final String CREATE_TABLE_ARTICLES = "CREATE TABLE "
            + TABLE_ARTICLES + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " TEXT NOT NULL UNIQUE, "
            + COLUMN_AVATAR + " TEXT NOT NULL, "
            + COLUMN_NICKNAME + " TEXT NOT NULL, "
            + COLUMN_TWEETTEXT + " TEXT NOT NULL" + ")";

    public static void onCreate(SQLiteDatabase db) {
        System.out.println("ArticlesTable.onCreate: " + CREATE_TABLE_ARTICLES);
        db.execSQL(CREATE_TABLE_ARTICLES);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        onCreate(db);
    }
}