package com.example.pratik.intouch_v_01;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pratik.intouch_v_01.model.News;
import com.google.firebase.analytics.FirebaseAnalytics;
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

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<News> newsList = new ArrayList<>();
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDatabase database;
    private Query queryNewRef;
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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Database instance
        database = FirebaseDatabase.getInstance();
        newsRef = database.getReference(getString(R.string.firebase_database_news));
        Query queryNewRef = newsRef.orderByChild("newsid");
        // Fetch data form Firebase
        fetch_news(queryNewRef);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Refresh the feed from server
        image_view_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.rotate_animation_refresh);
                image_view_refresh.startAnimation(anim);
                fetch_news(newsRef);
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

    public void fetch_news(Query newsRef){
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot childdataSnapshot :dataSnapshot.getChildren()){
                        News sNews = childdataSnapshot.getValue(News.class);
                        newsList.add(sNews);
                    }
                    Collections.reverse(newsList);
                }
                mSectionsPagerAdapter.notifyDataSetChanged();
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
            // Show 3 total pages.
            return newsList.size();
        }


    }
}
