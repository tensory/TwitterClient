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
import net.tensory.apps.icantbelieveitsnottwitter.models.StoredTweet;
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
	protected JsonHttpResponseHandler tweetRequestHandler; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		tweetRequestHandler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				jsonTweets = sanitizeStream(jsonTweets);
				saveTweets(jsonTweets);
				
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				TweetsAdapter tweetsAdapter = new TweetsAdapter(getBaseContext(), tweets);
				ListView lv = (ListView) findViewById(R.id.lvTweets);
				lv.setAdapter(tweetsAdapter);
			}
		};
		/*
		ArrayList<Tweet> tweets = Tweet.getAll();
		try {
			if (tweets.isEmpty()) {
				throw new Exception("Not enough tweets");
			}
			TweetsAdapter tweetsAdapter = new TweetsAdapter(getBaseContext(), tweets);
			ListView lv = (ListView) findViewById(R.id.lvTweets);
			lv.setAdapter(tweetsAdapter);
		} catch (Exception e) {
			
			
		}
		/*
		
		try {
			ArrayList<StoredTweet> storedTweets = StoredTweet.getAll();
			if (storedTweets.isEmpty()) {
				// Toast.makeText(getBaseContext(), "Not enough tweets", Toast.LENGTH_LONG).show();
				throw new Exception("Not enough tweets!");
			}
			Toast.makeText(getBaseContext(), "Using stored tweets", 60).show();
			StoredTweetsAdapter tweetsAdapter = new StoredTweetsAdapter(getBaseContext(), storedTweets);
			ListView lv = (ListView) findViewById(R.id.lvTweets);
			lv.setAdapter(tweetsAdapter);
		} catch (Exception e) {
			Log.d("ERROR", "no tweets stored");
			TwitterClientApp.getRestClient().getHomeTimeline(tweetRequestHandler);
		}*/
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
	
	private JSONArray sanitizeStream(JSONArray tweets) {
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
	
	protected void saveTweets(JSONArray tweets) {
		try {
			for (int i = 0; i < tweets.length(); i++) {
				StoredTweet st = new StoredTweet((JSONObject) tweets.get(i));
				st.save();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
