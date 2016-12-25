package com.example.pratik.intouch_v_01.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

public class inTouchSyncAdapter extends AbstractThreadedSyncAdapter {

    public DatabaseReference newsRef;
    private List<News> newsList = new ArrayList<>();
    private NewsDbHelper newsDbHelper;
    private SQLiteDatabase db;
    private static String newsCategory = null;
    public final String LOG_TAG = inTouchSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
//    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_INTERVAL = 10;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public inTouchSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        newsRef = database.getReference("news");
        newsDbHelper = new NewsDbHelper(getContext());
        db = newsDbHelper.getWritableDatabase();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        newsCategory = sp.getString("getNewsCategory", "all_News");

        Log.e("FetchAllNews ", "Data present in url " + newsRef);
//        Toast.makeText(getContext(), "Feeds Updated "+ " Test for service ", Toast.LENGTH_SHORT).show();
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
//                        Uri insertUri = getContext().getContentResolver().insert(NewsContract.NewsEntry.CONTENT_URI, values);
//                        if (insertUri == null) {
//                            Toast.makeText(getContext(), "Error with saving news", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    MainActivity mainActivity = new MainActivity();
//                    mainActivity.fetchNewsFromDatabase(newsCategory);
//                }
//                Toast.makeText(getContext(), "Feeds Updated ", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        inTouchSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }
}
