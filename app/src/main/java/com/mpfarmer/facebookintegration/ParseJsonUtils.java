package com.mpfarmer.facebookintegration;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpfarmer.facebookintegration.model.Friend;
import com.mpfarmer.facebookintegration.model.FriendDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class ParseJsonUtils {

    private static final String TAG = "ParseJsonUtils";

    public List<Friend> parseJsonObjectToFriendList(JSONObject jsonObject) {
        List<Friend> friendList = new ArrayList<>();
        try {
            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject obj;

                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(Friend.class, new FriendDeserializer());
                    Gson gson = builder.create();
                    Friend friend;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        obj = jsonArray.getJSONObject(i);
                        friend = gson.fromJson(obj.toString(), Friend.class);
                        friendList.add(friend);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "parsing Json Error", e);
        }
        return friendList;
    }
}
