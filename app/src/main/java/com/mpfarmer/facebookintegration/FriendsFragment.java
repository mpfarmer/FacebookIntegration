package com.mpfarmer.facebookintegration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.mpfarmer.facebookintegration.model.Friend;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mpfarmer.facebookintegration.FriendDetailActivity.EXTRA_FRIEND;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class FriendsFragment extends Fragment {

    public final static String TAG = "FriendsFragment";
    private static FriendsFragment mInstance;
    private View view;
    private RecyclerView rvFriendList;
    private LinearLayoutManager linearLayoutManager;
    private List<Friend> friendList = new ArrayList<>();
    private FriendsAdapter friendsAdapter;
    private TextView tvEmptyList;
    private GraphRequest nextRequest;

    public static synchronized FriendsFragment getInstance() {
        if (mInstance == null) {
            mInstance = new FriendsFragment();
        }
        return mInstance;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        if (!checkAccessToken()) {
            getActivity().finish();
        }
    }

    private boolean checkAccessToken() {
        if (AccessToken.getCurrentAccessToken() == null ||
                AccessToken.getCurrentAccessToken().isExpired()) {
            return false;
        }
        return true;
    }

    private boolean requestNextFriends(GraphRequest pendingRequest) {
        if (pendingRequest == null) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name,picture");
        pendingRequest.setParameters(bundle);
        pendingRequest.setCallback(DefaultCallback(new Callback() {
            @Override
            public void complete(GraphResponse graphResponse, JSONObject jsonObject) {
                onNextRequestComplete(graphResponse, jsonObject);
            }

            @Override
            public void fail() {
                onFriendsRequestFail();
            }
        }));
        pendingRequest.executeAsync();
        return true;
    }

    private void requestFriends() {
        if (friendList.size() > 0) {
            return;
        }
        Bundle params = new Bundle();
        params.putString("fields", "id,name,picture");
        params.putString("limit", "8");
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(),
                "me/taggable_friends",
                params,
                HttpMethod.GET,
                DefaultCallback(new Callback() {
                    @Override
                    public void complete(GraphResponse graphResponse, JSONObject jsonObject) {
                        onFriendsRequestComplete(graphResponse, jsonObject);
                    }

                    @Override
                    public void fail() {
                        onFriendsRequestFail();
                    }
                }));
        graphRequest.executeAsync();
    }

    public void onNextRequestComplete(GraphResponse graphResponse, JSONObject jsonObject) {
        Log.d(TAG, "next_jsonObject: " + jsonObject);
        List<Friend> newFriends = new ParseJsonUtils().parseJsonObjectToFriendList(jsonObject);

        friendList.remove(friendList.size() - 1);
        new Handler().post(new Runnable() {
            public void run() {
                friendsAdapter.notifyItemRemoved(friendList.size());
            }
        });
        int end = newFriends.size();

        for (int i = 0; i < end; i++) {
            friendList.add(newFriends.get(i));
            new Handler().post(new Runnable() {
                public void run() {
                    friendsAdapter.notifyItemInserted(friendList.size());
                }
            });
        }
        friendsAdapter.setLoaded();

        nextRequest = graphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
    }

    public void onFriendsRequestComplete(GraphResponse graphResponse, JSONObject jsonObject) {
        if (friendList == null)
        Log.d(TAG, "jsonObject: " + jsonObject);
        List<Friend> newFriends = new ParseJsonUtils().parseJsonObjectToFriendList(jsonObject);
        friendList.addAll(newFriends);
        friendsAdapter.notifyDataSetChanged();
        rvFriendList.setVisibility(View.VISIBLE);
        tvEmptyList.setVisibility(View.GONE);

        nextRequest = graphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
    }

    public void onFriendsRequestFail() {
        Log.d(TAG, "fail to get Friends");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView()");
        view = inflater.inflate(R.layout.fragment_list, container, false);
        rvFriendList = (RecyclerView) view.findViewById(R.id.friends_view);
        rvFriendList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFriendList.setLayoutManager(linearLayoutManager);
        friendsAdapter = new FriendsAdapter(friendList, rvFriendList, new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Friend item) {
                Intent intent = new Intent(getActivity(), FriendDetailActivity.class);
                intent.putExtra(EXTRA_FRIEND, item);
                getActivity().startActivity(intent);
            }
        });
        rvFriendList.setAdapter(friendsAdapter);

        tvEmptyList = (TextView) view.findViewById(R.id.tv_empty_list);
        if (friendList.isEmpty()) {
            rvFriendList.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            rvFriendList.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        }
        friendsAdapter.setLoadMoreListener(new FriendsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                friendList.add(null);
                new Handler().post(new Runnable() {
                    public void run() {
                        friendsAdapter.notifyItemInserted(friendList.size() - 1);
                    }
                });

                if (!requestNextFriends(nextRequest)) {
                    Log.d(TAG, "no more nextRequest");
                    friendList.remove(friendList.size() - 1);
                    new Handler().post(new Runnable() {
                        public void run() {
                            friendsAdapter.notifyItemRemoved(friendList.size());
                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestFriends();
    }

    private GraphRequest.Callback DefaultCallback(final Callback callback) {
        GraphRequest.Callback GraphRequestcallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if (graphResponse.getError() != null) {
                    callback.fail();
                    Log.d(TAG, "graphResponse Error: " + graphResponse.getError().getErrorMessage());
                } else {
                    callback.complete(graphResponse, graphResponse.getJSONObject());
                }
            }
        };
        return GraphRequestcallback;
    }


    public interface Callback {
        void complete(GraphResponse graphResponse, JSONObject jsonObject);

        void fail();
    }
}
