package net.tensory.apps.icantbelieveitsnottwitter.fragments;

import java.util.ArrayList;

import net.tensory.apps.icantbelieveitsnottwitter.EndlessScrollListener;
import net.tensory.apps.icantbelieveitsnottwitter.R;
import net.tensory.apps.icantbelieveitsnottwitter.TweetsAdapter;
import net.tensory.apps.icantbelieveitsnottwitter.TwitterClientApp;
import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;

import org.json.JSONArray;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {
	private TweetsListFragment fragmentTweets;
	private JsonHttpResponseHandler tweetRequestHandler;
	private long lastTweetId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fragmentTweets = (TweetsListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout);
		tweetRequestHandler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				jsonTweets = TweetsListFragment.sanitizeStream(jsonTweets);
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				fragmentTweets.getAdapter().addAll(tweets);
				lv.setOnScrollListener(new EndlessScrollListener() {
					@Override
					public void loadMore(int page, int totalItemsCount) {
						Tweet lastTweet = getAdapter().getItem(getAdapter().getCount() - 1);
						lastTweetId = lastTweet.getTweetId();
						
						TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONArray newJson) {
								newJson = TweetsListFragment.sanitizeStream(newJson);
								ArrayList<Tweet> newTweets = Tweet.fromJson(newJson);
								
								// Delete the first tweet if its tweetID is the same as the oldest in the last batch
								if (newTweets.get(0).getTweetId() == lastTweetId) {
									newTweets.remove(0);
								}
								
								// Get the adapter for the static ListView in the parent JsonHttpResponseHandler,
								// and assign newTweets to it
								TweetsAdapter tweetListAdapter = (TweetsAdapter) getAdapter();
								tweetListAdapter.addAll(newTweets);
								tweetListAdapter.notifyDataSetChanged();
							}
							
						}, lastTweetId);
					}
				});
			}
		};
				
		ArrayList<Tweet> tweets = Tweet.getAll();
		try {
			if (tweets.isEmpty()) {
				throw new Exception("Not enough tweets");
			}
			TweetsAdapter tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
			lv.setAdapter(tweetsAdapter);
		} catch (Exception e) {
			TwitterClientApp.getRestClient().getHomeTimeline(tweetRequestHandler);			
		}
	}
}
