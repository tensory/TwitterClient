package net.tensory.apps.icantbelieveitsnottwitter;

import net.tensory.apps.icantbelieveitsnottwitter.models.User;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ProfileImageOnClickListener implements OnClickListener {
	User user;
	Context context;
	
	public ProfileImageOnClickListener(Context c, User u) {
		user = u;
		context = c;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent i = new Intent(context, ProfileActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("userScreenName", user.getScreenName());
		context.startActivity(i);
		
		TwitterClientApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userData) {
				try {
					Log.d("IMAGE_USER", user.getScreenName());
					

				} catch (Exception e) {
					Log.e("BAD_CONTEXT", e.getStackTrace().toString());
				}
			} 
		}, user.getScreenName());
		
	}
}
