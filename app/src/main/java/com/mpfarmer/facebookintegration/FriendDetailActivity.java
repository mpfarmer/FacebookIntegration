package com.mpfarmer.facebookintegration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mpfarmer.facebookintegration.model.Friend;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class FriendDetailActivity extends AppCompatActivity {

    public static final String EXTRA_FRIEND = "EXTRA_FRIEND";

    private CircleImageView circleImageView;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        getExtrasFromIntent();
        circleImageView = (CircleImageView) findViewById(R.id.civ_friend_image);
        Glide.with(this).load(friend.getPicture().url).into(circleImageView);
    }

    private void getExtrasFromIntent() {
        friend = (Friend) getIntent().getSerializableExtra(EXTRA_FRIEND);
        setTitle(friend.getName());
    }

}
