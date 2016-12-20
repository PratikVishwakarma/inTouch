package com.example.pratik.intouch_v_01.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by prati on 21-Oct-16.
 */

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "news.db";


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

    public void addNews(String news_id, String Date, String Time, String Headline, String Content, String Image, String Category, String Source, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        //Log.e("add ", "addNews: "+ Category);
        contentValues.put(NewsContract.NewsEntry.COLUMN_NEWSID, news_id);
        contentValues.put(NewsContract.NewsEntry.COLUMN_DATE, Date);
        contentValues.put(NewsContract.NewsEntry.COLUMN_TIME, Time);
        contentValues.put(NewsContract.NewsEntry.COLUMN_HEADLINE, Headline);
        contentValues.put(NewsContract.NewsEntry.COLUMN_NEWS_CONTENT, Content);
        contentValues.put(NewsContract.NewsEntry.COLUMN_IMAGE, Image);
        contentValues.put(NewsContract.NewsEntry.COLUMN_CATEGORY, Category);
        contentValues.put(NewsContract.NewsEntry.COLUMN_SOURCE, Source);
        db.insert(NewsContract.NewsEntry.TABLE_NAME, null, contentValues);
    }

    public void deleteNews(SQLiteDatabase sqLiteDatabase){
        Log.e("FetchAllNews ", "delete entries from table");
        sqLiteDatabase.execSQL("DELETE FROM "+ NewsContract.NewsEntry.TABLE_NAME );
    }

    public Cursor getAllNews(SQLiteDatabase db, String category){
        Cursor cursor;
        String[] select_args = {category};
        String projection[] = {
                NewsContract.NewsEntry.COLUMN_NEWSID,
                NewsContract.NewsEntry.COLUMN_HEADLINE,
                NewsContract.NewsEntry.COLUMN_NEWS_CONTENT,
                NewsContract.NewsEntry.COLUMN_DATE,
                NewsContract.NewsEntry.COLUMN_TIME,
                NewsContract.NewsEntry.COLUMN_IMAGE,
                NewsContract.NewsEntry.COLUMN_CATEGORY
        };
        if(category.equals("all")){
            cursor = db.query(NewsContract.NewsEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    NewsContract.NewsEntry.COLUMN_NEWSID+ " DESC");
        }else{
            cursor = db.query(NewsContract.NewsEntry.TABLE_NAME,
                    projection,
                    NewsContract.NewsEntry.COLUMN_CATEGORY + " =?",
                    select_args,
                    null,
                    null,
                    NewsContract.NewsEntry.COLUMN_NEWSID+ " DESC");
        }

        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ NewsContract.NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
