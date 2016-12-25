package com.example.pratik.intouch_v_01.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by prati on 26-Dec-16.
 */

public class inTouchAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private inTouchAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new inTouchAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
