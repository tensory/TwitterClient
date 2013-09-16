package net.tensory.apps.icantbelieveitsnottwitter.fragments;

import java.util.ArrayList;

import net.tensory.apps.icantbelieveitsnottwitter.R;
import net.tensory.apps.icantbelieveitsnottwitter.TwitterClientApp;
import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		final TweetsListFragment fragmentTweets = (TweetsListFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
		
		super.onCreate(savedInstanceState);
		TwitterClientApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				jsonTweets = TweetsListFragment.sanitizeStream(jsonTweets);
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				fragmentTweets.getAdapter().addAll(tweets);	
			}
			
			@Override 
			public void onFailure(Throwable error) {
				Log.d("MENTIONS_ERROR", error.getMessage());
			}
		});
	}

}
