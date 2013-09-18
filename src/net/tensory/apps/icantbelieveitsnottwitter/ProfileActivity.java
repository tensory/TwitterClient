package net.tensory.apps.icantbelieveitsnottwitter;

import net.tensory.apps.icantbelieveitsnottwitter.models.User;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		String userScreenName = "";
		try {
			userScreenName = getIntent().getExtras().getString("userScreenName");
		} catch (Exception e) {
			Log.e("USERNAME_NOT_PROVIDED", e.getStackTrace().toString());
		}
		
		if (userScreenName.length() > 0) {
			loadProfileInfo(userScreenName);
		} else {
			loadProfileInfo();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	private void populateProfileHeader(User user) {
		TextView tvFullName = (TextView) findViewById(R.id.tvFullName);
		TextView tvUserTagline = (TextView) findViewById(R.id.tvUserTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvFullName.setText(user.getName());
		tvUserTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowersCount() + " followers");
		tvFollowing.setText(user.getFollowingCount() + " following");
		// load profile image
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
	}
	
	private void loadProfileInfo() {
		TwitterClientApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userData) {
				User u = User.fromJson(userData);
				getActionBar().setTitle("@" + u.getScreenName());
				populateProfileHeader(u);
				Log.d("USER_DATA", userData.toString());
			} 
		});
	}
	
	private void loadProfileInfo(String screenName) {
		TwitterClientApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userData) {
				User u = User.fromJson(userData);
				getActionBar().setTitle("@" + u.getScreenName());
				populateProfileHeader(u);
				Log.d("USER_DATA", userData.toString());
			} 
		}, screenName);
	}

}
