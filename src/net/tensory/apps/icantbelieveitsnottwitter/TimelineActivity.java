package net.tensory.apps.icantbelieveitsnottwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.tensory.apps.icantbelieveitsnottwitter.R;
import net.tensory.apps.icantbelieveitsnottwitter.R.layout;
import net.tensory.apps.icantbelieveitsnottwitter.R.menu;
import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends Activity {
	public static final int COMPOSE_ACTIVITY_ID = 2;
	private TimelineJsonHttpResponseHandler tweetRequestHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		ListView lv = (ListView) findViewById(R.id.lvTweets);
		
		tweetRequestHandler = new TimelineJsonHttpResponseHandler(getApplicationContext()) {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				jsonTweets = TimelineActivity.sanitizeStream(jsonTweets);
				
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				ListView lv = (ListView) findViewById(R.id.lvTweets);
				TweetsAdapter tweetsAdapter = new TweetsAdapter(getBaseContext(), tweets);
				lv.setAdapter(tweetsAdapter);
				
				this.setListView(lv);
			}
		};
		
		ArrayList<Tweet> tweets = Tweet.getAll();
		try {
			if (tweets.isEmpty()) {
				Toast.makeText(getBaseContext(), "pulling from storage DIDN'T WORK", Toast.LENGTH_LONG).show();
				throw new Exception("Not enough tweets");
			}
			Toast.makeText(getBaseContext(), "pulling from storage", Toast.LENGTH_LONG).show();
			TweetsAdapter tweetsAdapter = new TweetsAdapter(getBaseContext(), tweets);
			lv.setAdapter(tweetsAdapter);
			tweetRequestHandler.setListView(lv);
		} catch (Exception e) {
			TwitterClientApp.getRestClient().getHomeTimeline(tweetRequestHandler);			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_compose:
	      startComposeAction();
	      break;
	    default:
	    	break;
	    }
	    return true;
	  }
	
	protected static JSONArray sanitizeStream(JSONArray tweets) {
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
	
	private void startComposeAction() {
		Intent i = new Intent(getBaseContext(), ComposeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(i, COMPOSE_ACTIVITY_ID);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
	          Intent data) {
	      if (requestCode == COMPOSE_ACTIVITY_ID) {
	          if (resultCode == RESULT_OK) {
	      		TwitterClientApp.getRestClient().getHomeTimeline(tweetRequestHandler);
	          }
	      }
	}
	
	protected abstract static class TimelineJsonHttpResponseHandler extends JsonHttpResponseHandler {
		public static ListView tweetList;
		public static Context context;
		public static long lastTweetId;
		
		public TimelineJsonHttpResponseHandler() {
			super();
		}
		
		public TimelineJsonHttpResponseHandler(Context mContext) {
			super();
			context = mContext;
		}
			
		@Override
		public abstract void onSuccess(JSONArray jsonTweets);
		
		public void setListView(ListView myTweetList) {
			TimelineJsonHttpResponseHandler.tweetList = myTweetList;
			setListViewEvents();
		}
		
		protected static void setLastTweetId(long id) {
			TimelineJsonHttpResponseHandler.lastTweetId = id;
		}
		
		private void setListViewEvents() {
			try {
				tweetList.setOnScrollListener(new EndlessScrollListener() {
					@Override
					public void loadMore(int page, int totalItemsCount) {
						Tweet lastTweet = (Tweet) tweetList.getItemAtPosition(tweetList.getAdapter().getCount() - 1);
						TimelineJsonHttpResponseHandler.setLastTweetId(lastTweet.getTweetId());
						TwitterClientApp.getRestClient().getOlderTimeline(new TimelineJsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONArray newJson) {
								newJson = TimelineActivity.sanitizeStream(newJson);
								ArrayList<Tweet> newTweets = Tweet.fromJson(newJson);
								
								// Get the adapter for the static ListView in the parent JsonHttpResponseHandler,
								// and assign newTweets to it
								TweetsAdapter tweetListAdapter = (TweetsAdapter) TimelineJsonHttpResponseHandler.tweetList.getAdapter();
								tweetListAdapter.addAll(newTweets);
								tweetListAdapter.notifyDataSetChanged();
								//Toast.makeText(TimelineJsonHttpResponseHandler.context, "" + newJson.length(), Toast.LENGTH_SHORT).show();
							}
						}, TimelineJsonHttpResponseHandler.lastTweetId);
					}
				});	
			} catch (NullPointerException npe) {
				Log.e("LIST_VIEW_EXCEPTION", npe.getStackTrace().toString());
			}
		}
	}
}
