package net.tensory.apps.icantbelieveitsnottwitter;

import net.tensory.apps.icantbelieveitsnottwitter.models.User;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		loadProfileInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	private void loadProfileInfo() {
		TwitterClientApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userData) {
				User u = User.fromJson(userData);
				
				getActionBar().setTitle("@" + u.getScreenName());
				
				Log.d("USER_DATA", userData.toString());
			} 
		});
	}

}
