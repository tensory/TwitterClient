package net.tensory.apps.icantbelieveitsnottwitter;

import java.util.ArrayList;

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
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				jsonTweets = sanitizeStream(jsonTweets);
				
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
				ListView lv = (ListView) findViewById(R.id.lvTweets);
				lv.setAdapter(adapter);
			}
		});
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
				if (tweet.getString("text").contains("Dymaxion") == false) {
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
		startActivity(i);
	}
}
