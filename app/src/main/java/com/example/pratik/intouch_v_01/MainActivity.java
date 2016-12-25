package com.example.pratik.intouch_v_01;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.pratik.intouch_v_01.service.inTouchService;
import com.example.pratik.intouch_v_01.sync.inTouchSyncAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<News> newsList = new ArrayList<>();

//    private NewsDbHelper newsDbHelper;
//    private SQLiteDatabase db;

//    private DatabaseReference newsRef;

    private static String newsCategory = null;

    private ImageView image_view_refresh, image_view_setting;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeScreen();

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Database instance

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        newsCategory = sp.getString(getResources().getString(R.string.key_get_news_category), getResources().getString(R.string.key_news_category_allNews));

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        newsRef = database.getReference(getString(R.string.firebase_database_news));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Instantiate a ViewPager and a PagerAdapter.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), newsList);
        mViewPager.setAdapter(mSectionsPagerAdapter);

//        newsDbHelper = new NewsDbHelper(getBaseContext());
//        db = newsDbHelper.getWritableDatabase();

        if (isNetworkAvailable()) {
            fetchNewsFromDatabase(newsCategory);
            // Fetch data form Firebase
//            fetchNewsFromFirebase();
        } else {
//            fetchNewsFromDatabase(newsCategory);
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_message_network_not_available), Toast.LENGTH_SHORT).show();
        }

        // Refresh the feed from server
        image_view_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.rotate_animation_refresh);
                image_view_refresh.startAnimation(anim);
                fetchNewsFromFirebase();
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
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchNewsFromFirebase() {
        //For syncAdapter
        inTouchSyncAdapter.syncImmediately(getApplicationContext());

        // For intent Services
//        Intent alarmIntent = new Intent(this, inTouchService.AlarmReceiver.class);
//
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pi);
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

    public void fetchNewsFromDatabase(String newsCategory) {
        getSupportLoaderManager().initLoader(1, null, this);
    }


    private void initializeScreen() {
        mViewPager = (ViewPager) findViewById(R.id.container);
        image_view_refresh = (ImageView) findViewById(R.id.image_view_refresh_icon);
        image_view_setting = (ImageView) findViewById(R.id.image_view_setting_icon);
        setupWindowAnimations();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
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

        String sortOrder = NewsContract.NewsEntry.COLUMN_ID + " ASC";
        Uri uri = Uri.withAppendedPath(NewsContract.NewsEntry.CONTENT_URI, newsCategory);

        CursorLoader cursorLoader = new CursorLoader(this);
        cursorLoader.setUri(uri);
        cursorLoader.setSelection(null);
        cursorLoader.setSelectionArgs(null);
        cursorLoader.setProjection(projection);
        cursorLoader.setSortOrder(sortOrder);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursorData) {
        if (cursorData.moveToFirst()) {
            int columnCount = cursorData.getColumnCount();
            Log.e("FetchAllNews ", "Data present in database " + columnCount + "");
            newsList.clear();
            do {
                String headline, content, date, time, image, ncategory, source;
                Long newsid;
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
            } while (cursorData.moveToNext());
            //Collections.reverse(newsList);
            mSectionsPagerAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_message_no_data_available), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm, List<News> newsList) {
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
