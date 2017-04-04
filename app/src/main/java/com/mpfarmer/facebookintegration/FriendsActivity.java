package com.mpfarmer.facebookintegration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class FriendsActivity extends AppCompatActivity {

    private final static String TAG = "FriendsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() ");
        setContentView(R.layout.activity_friends);
        if (savedInstanceState == null) {
            Log.d(TAG, "savedInstanceState == null, transaction add");
            FriendsFragment fragment = FriendsFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_list, fragment);
            transaction.addToBackStack(FriendsFragment.TAG);
            transaction.commit();
        } else {
            Log.d(TAG, "savedInstanceState != null");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() ");
    }

    @Override
    public void onBackPressed() {
        finish();
        Log.d(TAG, "onBackPressed() ");
    }
}
