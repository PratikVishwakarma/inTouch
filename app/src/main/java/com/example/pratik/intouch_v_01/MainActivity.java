package com.example.pratik.intouch_v_01;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pratik.intouch_v_01.database.NewsContract;
import com.example.pratik.intouch_v_01.database.NewsDbHelper;
import com.example.pratik.intouch_v_01.model.News;
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<News> newsList = new ArrayList<>();

    private NewsDbHelper newsDbHelper;
    SQLiteDatabase db;
    Cursor cursorData;

//    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDatabase database;
    private DatabaseReference newsRef;


    private ImageView image_view_refresh, image_view_setting;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        initializeScreen();

        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Database instance
        database = FirebaseDatabase.getInstance();
        newsRef = database.getReference(getString(R.string.firebase_database_news));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Instantiate a ViewPager and a PagerAdapter.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), newsList);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        newsDbHelper = new NewsDbHelper(getBaseContext());
        db = newsDbHelper.getWritableDatabase();

        if(isNetworkAvailable()){
            // Fetch data form Firebase
            fetch_news_from_database();
            fetch_news_from_firebase(newsRef);
        }else{
            fetch_news_from_database();
            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
        }

        // Refresh the feed from server
        image_view_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.rotate_animation_refresh);
                image_view_refresh.startAnimation(anim);
                fetch_news_from_firebase(newsRef);
            }
        });

        image_view_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), newsList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                invalidateOptionsMenu();
//            }
//        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void fetch_news_from_firebase(Query newsRef){
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear();
                if(dataSnapshot.exists()){
                    newsDbHelper.deleteNews(db);
                    for(DataSnapshot childdataSnapshot :dataSnapshot.getChildren()){
                        News sNews = childdataSnapshot.getValue(News.class);
                        ContentValues values = new ContentValues();
                        values.put(NewsContract.NewsEntry.COLUMN_NEWSID, sNews.getNewsid()+"");
                        values.put(NewsContract.NewsEntry.COLUMN_DATE, sNews.getDate());
                        values.put(NewsContract.NewsEntry.COLUMN_TIME, sNews.getTime());
                        values.put(NewsContract.NewsEntry.COLUMN_HEADLINE, sNews.getHeadline());
                        values.put(NewsContract.NewsEntry.COLUMN_NEWS_CONTENT, sNews.getNews_content());
                        values.put(NewsContract.NewsEntry.COLUMN_IMAGE, sNews.getImage());
                        values.put(NewsContract.NewsEntry.COLUMN_CATEGORY, sNews.getCategory());
                        values.put(NewsContract.NewsEntry.COLUMN_SOURCE, sNews.getSource());

                        Uri insertUri = getContentResolver().insert(NewsContract.NewsEntry.CONTENT_URI, values);
//                        long newRowId = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);
//                        if (newRowId == -1) {
//                            // If the row ID is -1, then there was an error with insertion.
//                            Toast.makeText(getApplicationContext(), "Error with saving news", Toast.LENGTH_SHORT).show();
//                        }
                    }
                    fetch_news_from_database();
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_feeds_updated),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupWindowAnimations() {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetch_news_from_database(){
//        SQLiteDatabase db = newsDbHelper.getReadableDatabase();

        String projection[] = {
                NewsContract.NewsEntry.COLUMN_NEWSID,
                NewsContract.NewsEntry.COLUMN_HEADLINE,
                NewsContract.NewsEntry.COLUMN_NEWS_CONTENT,
                NewsContract.NewsEntry.COLUMN_DATE,
                NewsContract.NewsEntry.COLUMN_TIME,
                NewsContract.NewsEntry.COLUMN_IMAGE,
                NewsContract.NewsEntry.COLUMN_CATEGORY,
                NewsContract.NewsEntry.COLUMN_SOURCE
        };

//        cursorData = db.query(NewsContract.NewsEntry.TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                NewsContract.NewsEntry.COLUMN_NEWSID+ " DESC");
        Uri uri = NewsContract.NewsEntry.CONTENT_URI;
        cursorData = getContentResolver().query(uri, projection, null, null, null);

        try{
            if(cursorData.moveToFirst()){
                int columnCount = cursorData.getColumnCount();
                Log.e("FetchAllNews ", "Data present in database "+columnCount+"");
                newsList.clear();
                do {
                    String headline, content, date, time, image, ncategory, source;
                    Long newsid;
                    int id;

                    int newsIdColumnIndex = cursorData.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWSID);
                    int contentColumnIndex = cursorData.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_CONTENT);
                    int headlineColumnIndex = cursorData.getColumnIndex(NewsContract.NewsEntry.COLUMN_HEADLINE);
                    int dateColumnIndex = cursorData.getColumnIndex(NewsContract.NewsEntry.COLUMN_DATE);
                    int timeColumnIndex = cursorData.getColumnIndex(NewsContract.NewsEntry.COLUMN_TIME);
                    int imageColumnIndex = cursorData.getColumnIndex(NewsContract.NewsEntry.COLUMN_IMAGE);
                    int categoryColumnIndex = cursorData.getColumnIndex(NewsContract.NewsEntry.COLUMN_CATEGORY);
                    int sourceColumnIndex = cursorData.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE);

                    newsid = cursorData.getLong(newsIdColumnIndex);
                    headline = cursorData.getString(headlineColumnIndex);
                    content = cursorData.getString(contentColumnIndex);
                    date = cursorData.getString(dateColumnIndex);
                    time = cursorData.getString(timeColumnIndex);
                    image = cursorData.getString(imageColumnIndex);
                    ncategory = cursorData.getString(categoryColumnIndex);
                    source = cursorData.getString(sourceColumnIndex);
                    News newsProvider = new News(headline, content, newsid, image, date, time, ncategory, source);
                    newsList.add(newsProvider);
                    Collections.reverse(newsList);
                }while (cursorData.moveToNext());
                mSectionsPagerAdapter.notifyDataSetChanged();
            }
        }finally {
            cursorData.close();
        }

    }


    public void initializeScreen(){
        mViewPager = (ViewPager) findViewById(R.id.container);
        image_view_refresh = (ImageView) findViewById(R.id.image_view_refresh_icon);
        image_view_setting = (ImageView) findViewById(R.id.image_view_setting_icon);
        setupWindowAnimations();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm, List <News> newsList) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            Toast.makeText(getApplicationContext(), position+"",Toast.LENGTH_SHORT).show();
            return NewsHolderFragment.newInstance(position, newsList.get(position));
        }

        @Override
        public int getCount() {
            return newsList.size();
        }


    }
}
