package com.example.pratik.intouch_v_01.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by prati on 21-Oct-16.
 */

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "inTouchNews.db";


    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE "+ NewsContract.NewsEntry.TABLE_NAME + " (" +
                NewsContract.NewsEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsContract.NewsEntry.COLUMN_NEWSID +" TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_DATE +" TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_TIME +" TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_HEADLINE +" TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_NEWS_CONTENT +" TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_IMAGE +" TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_CATEGORY +" TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_SOURCE +" TEXT NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_TABLE);
        Log.e("Database ", "Table Created");
    }

    public void deleteNews(SQLiteDatabase sqLiteDatabase){
        Log.e("FetchAllNews ", "delete entries from table");
        sqLiteDatabase.execSQL("DELETE FROM "+ NewsContract.NewsEntry.TABLE_NAME );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ NewsContract.NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
