package net.tensory.apps.icantbelieveitsnottwitter;

import java.util.ArrayList;

import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class ComposeActivity extends Activity {
	Button btnPostTweet;
	EditText etCompose;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    etCompose = (EditText) findViewById(R.id.etCompose);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	// Suppress transition animation on Back press.
	// From http://stackoverflow.com/questions/6959043/cannot-disable-transition-animation-when-back-button-is-clicked
	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}
	
	public void onSubmitTweet(MenuItem mi) {
		String raw = etCompose.getText().toString();
		TwitterClientApp.getRestClient().postTweet(raw, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				setResult(Activity.RESULT_OK, new Intent(getBaseContext(), TimelineActivity.class));
				finish();
			}
			
			@Override
			public void onFailure(Throwable exception, JSONObject response) {
				// TODO Auto-generated method stub
				Log.d("ERROR", response.toString());
				Log.d("ERROR", exception.getMessage());
				Toast.makeText(getBaseContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
	}
}


