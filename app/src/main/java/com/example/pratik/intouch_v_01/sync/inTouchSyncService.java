package com.example.pratik.intouch_v_01.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by prati on 26-Dec-16.
 */

public class inTouchSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static inTouchSyncAdapter inTouchSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (inTouchSyncAdapter == null) {
                inTouchSyncAdapter  = new inTouchSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return inTouchSyncAdapter.getSyncAdapterBinder();
    }
}
