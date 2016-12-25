package com.example.pratik.intouch_v_01.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.pratik.intouch_v_01.MainActivity;
import com.example.pratik.intouch_v_01.R;
import com.example.pratik.intouch_v_01.database.NewsContract;
import com.example.pratik.intouch_v_01.database.NewsDbHelper;
import com.example.pratik.intouch_v_01.model.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prati on 26-Dec-16.
 */

public class inTouchService extends IntentService {

    public DatabaseReference newsRef;
    private List<News> newsList = new ArrayList<>();
    private NewsDbHelper newsDbHelper;
    private SQLiteDatabase db;
    private static String newsCategory = null;

    public inTouchService() {
        super("inTouchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        newsRef = database.getReference(getString(R.string.firebase_database_news));
        newsDbHelper = new NewsDbHelper(getBaseContext());
        db = newsDbHelper.getWritableDatabase();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        newsCategory = sp.getString(getResources().getString(R.string.key_get_news_category), getResources().getString(R.string.key_news_category_allNews));

        Log.e("FetchAllNews ", "Data present in url " + newsRef);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_message_feeds_updated)+ " Test for service ", Toast.LENGTH_SHORT).show();
//        newsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                newsList.clear();
//                if (dataSnapshot.exists()) {
//                    newsDbHelper.deleteNews(db);
//                    for (DataSnapshot childdataSnapshot : dataSnapshot.getChildren()) {
//                        News sNews = childdataSnapshot.getValue(News.class);
//                        ContentValues values = new ContentValues();
//                        values.put(NewsContract.NewsEntry.COLUMN_NEWSID, sNews.getNewsid() + "");
//                        values.put(NewsContract.NewsEntry.COLUMN_DATE, sNews.getDate());
//                        values.put(NewsContract.NewsEntry.COLUMN_TIME, sNews.getTime());
//                        values.put(NewsContract.NewsEntry.COLUMN_HEADLINE, sNews.getHeadline());
//                        values.put(NewsContract.NewsEntry.COLUMN_NEWS_CONTENT, sNews.getNews_content());
//                        values.put(NewsContract.NewsEntry.COLUMN_IMAGE, sNews.getImage());
//                        values.put(NewsContract.NewsEntry.COLUMN_CATEGORY, sNews.getCategory());
//                        values.put(NewsContract.NewsEntry.COLUMN_SOURCE, sNews.getSource());
//
//                        Uri insertUri = getContentResolver().insert(NewsContract.NewsEntry.CONTENT_URI, values);
//                        if (insertUri == null) {
//                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_message_error_with_saving_news), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    MainActivity mainActivity = new MainActivity();
//                    mainActivity.fetchNewsFromDatabase(newsCategory);
//                }
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_message_feeds_updated), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
    }
    public static class AlarmReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            intent = new Intent(context, inTouchService.class);
            context.startService(intent);
        }
    }
}
