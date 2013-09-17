package net.tensory.apps.icantbelieveitsnottwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.tensory.apps.icantbelieveitsnottwitter.EndlessScrollListener;
import net.tensory.apps.icantbelieveitsnottwitter.R;
import net.tensory.apps.icantbelieveitsnottwitter.TweetsAdapter;
import net.tensory.apps.icantbelieveitsnottwitter.TwitterClientApp;
import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class TweetsListFragment extends Fragment {
	private TweetsAdapter tweetsAdapter;
	protected ListView lv;
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		View v = inf.inflate(R.layout.fragment_tweets_list, parent, false);
		lv = (ListView) v.findViewById(R.id.lvTweets);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); // ask exactly what is going on here
		
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		lv = (ListView) getActivity().findViewById(R.id.lvTweets);
		tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
		lv.setAdapter(tweetsAdapter);
	}
	
	public TweetsAdapter getAdapter() {
		return tweetsAdapter;
	}
	
	public ListView getListView() {
		return lv;
	}
	
	public static JSONArray sanitizeStream(JSONArray tweets) {
		/* Tweet censoring, because I don't want to see certain retweets during development */
		JSONArray sanitized = new JSONArray();
		for (int i = 0; i < tweets.length(); i++) {
			JSONObject tweet;
			try {
				tweet = tweets.getJSONObject(i);
				if (tweet.getString("text").contains("Dymaxion") == false && tweet.getString("text").contains("Shanley") == false) {
					sanitized.put((JSONObject) tweet);
				}
			} catch (JSONException e) {
				Log.e("BAD_TWEET", e.getStackTrace().toString());
			}
		}
		return sanitized;
	}
	
	public abstract class TimelineJsonHttpResponseHandler extends JsonHttpResponseHandler {
		public ListView tweetList;
		private long lastTweetId;
		
		public TimelineJsonHttpResponseHandler() {
			super();
		}
		
		public TimelineJsonHttpResponseHandler(long lastTweetId) {
			super();
			this.lastTweetId = lastTweetId;
		}
			
		@Override
		public abstract void onSuccess(JSONArray jsonTweets);
		
		@Override 
		public void onFailure(Throwable error) {
			Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
			Log.d("TIMELINE_ERROR", error.getMessage());
		}
		
		public void setListView(ListView myTweetList) {
			tweetList = myTweetList;
			setListViewEvents();
		}
		
		private void setListViewEvents() {
			try {
				tweetList.setOnScrollListener(new EndlessScrollListener() {
					@Override
					public void loadMore(int page, int totalItemsCount) {
						Tweet lastTweet = getAdapter().getItem(getAdapter().getCount() - 1);
						lastTweetId = lastTweet.getTweetId();
						
						TwitterClientApp.getRestClient().getHomeTimeline(new TimelineJsonHttpResponseHandler() {	
							@Override
							public void onSuccess(JSONArray newJson) {
								Log.d("yes im nuts", newJson.toString());
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
			} catch (NullPointerException npe) {
				Log.e("LIST_VIEW_EXCEPTION", npe.getStackTrace().toString());
			}
		}
	}
}
