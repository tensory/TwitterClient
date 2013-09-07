package net.tensory.apps.icantbelieveitsnottwitter;

import java.util.ArrayList;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.tensory.apps.icantbelieveitsnottwitter.R;
import net.tensory.apps.icantbelieveitsnottwitter.R.layout;
import net.tensory.apps.icantbelieveitsnottwitter.R.menu;
import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class TimelineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
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

}
