package com.mpfarmer.facebookintegration;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class FacebookIntegrationApp extends Application {

    public static FacebookIntegrationApp get(Context context) {
        return (FacebookIntegrationApp) context.getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupFacebookAPI();
    }


    private void setupFacebookAPI() {
        FacebookSdk.setApplicationId(getString(R.string.app_id_facebook));
        FacebookSdk.sdkInitialize(this);
        FacebookSdk.setIsDebugEnabled(BuildConfig.DEBUG);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }
}
