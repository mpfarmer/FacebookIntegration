package com.mpfarmer.facebookintegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class LoginActivity extends AppCompatActivity implements FaceBookLoginFragment.FriendsPermissionListener {

    private final static String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            Log.d(TAG, "savedInstanceState == null, transaction add");
            FaceBookLoginFragment fragment = FaceBookLoginFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_login, fragment);
            transaction.addToBackStack(FaceBookLoginFragment.TAG);
            transaction.commit();
        } else {
            Log.d(TAG, "savedInstanceState != null");
        }
        checkPermissions();
    }

    private void checkPermissions() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "AccessToken: " + token);
        if (token != null) {
            Log.d(TAG, "isExpired: " + token.isExpired());
            if (!token.isExpired()) {
                Set<String> permissions = token.getPermissions();
                List<String> expectedPermList = Arrays.asList("user_friends", "user_posts");
                token.getPermissions().containsAll(expectedPermList);
                if (permissions.containsAll(expectedPermList)) {
                    Log.d(TAG, "startFriendsActivity()");
                    startFriendsActivity();
                } else {
                    Log.d(TAG, "LoginManager.getInstance().logInWithReadPermissions");
                    LoginManager.getInstance().logInWithReadPermissions(this, expectedPermList);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() requestCode: " + requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
    }

    public void startFriendsActivity() {
        startActivity(new Intent(this, FriendsActivity.class));
    }

    @Override
    public void onFriendsPermissionObtained() {
        Log.d(TAG, "onFriendsPermissionObtained()" );
        startFriendsActivity();
    }
}

