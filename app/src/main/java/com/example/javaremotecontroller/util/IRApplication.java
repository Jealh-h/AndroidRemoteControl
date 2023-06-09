package com.example.javaremotecontroller.util;

import android.util.Log;
import com.activeandroid.ActiveAndroid;
import net.irext.webapi.WebAPIs;
import net.irext.webapi.model.UserApp;
import net.irext.webapi.WebAPICallbacks.SignInCallback;

public class IRApplication extends com.activeandroid.app.Application {

    private static final String TAG = "WEB_API_DEBUG";
    private static final String ADDRESS = "http://irext.net";
    private static final String APP_NAME = "/irext-server";

    public WebAPIs mWeAPIs = WebAPIs.getInstance(ADDRESS, APP_NAME);
    public static UserApp mUserApp;

    private SignInCallback mSignInCallback = new SignInCallback() {
        @Override
        public void onSignInSuccess(UserApp userApp) {
            mUserApp = userApp;
            Log.e(TAG, "onSignInSuccess: " + userApp.getId() );
        }

        @Override
        public void onSignInFailed() {
            ToastUtils.showToastInUiThread(getApplicationContext(),"登录失败,请检查网络");
        }

        @Override
        public void onSignInError() {
            ToastUtils.showToastInUiThread(getApplicationContext(), "登录失败,请检查网络");
        }
    };

    public UserApp getUserApp() {
        return mUserApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize ActiveAndroid
        ActiveAndroid.initialize(this);
        Log.e(TAG, "signIn");
        // login with guest-admin account
        signIn();
    }

    public void signIn() {
        new Thread() {
            @Override
            public void run() {
                mWeAPIs.signIn(IRApplication.this, mSignInCallback);
                if (null != mUserApp) {
                    Log.e(TAG, "signIn response : " + mUserApp.getId() + ", " + mUserApp.getToken());
                } else {
                    Log.e(TAG, "signIn failed");
                }
            }
        }.start();
    }
}
