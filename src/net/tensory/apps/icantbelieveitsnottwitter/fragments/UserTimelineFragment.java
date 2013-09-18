package net.tensory.apps.icantbelieveitsnottwitter.fragments;

import net.tensory.apps.icantbelieveitsnottwitter.TwitterClientApp;
import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;

import org.json.JSONArray;

import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				jsonTweets = TweetsListFragment.sanitizeStream(jsonTweets);
				getAdapter().addAll(Tweet.fromJson(jsonTweets));
			}
		});
	}
	/*
		TODO
		
		
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
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
						
						TwitterClientApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
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
		}
	}
*/

	
	public UserTimelineFragment() {
		// TODO Auto-generated constructor stub
	}

}
