package com.mpfarmer.facebookintegration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mpfarmer.facebookintegration.model.Friend;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class FriendDetailActivity extends AppCompatActivity {

    private static final String EXTRA_FRIEND = "EXTRA_FRIEND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        getExtrasFromIntent();
    }


    public static Intent launchDetail(Context context, Friend friend) {
        Intent intent = new Intent(context, FriendDetailActivity.class);
        intent.putExtra(EXTRA_FRIEND, friend);
        return intent;
    }

    private void getExtrasFromIntent() {
        Friend friend = (Friend) getIntent().getSerializableExtra(EXTRA_FRIEND);
        setTitle(friend.getName());
    }

}
