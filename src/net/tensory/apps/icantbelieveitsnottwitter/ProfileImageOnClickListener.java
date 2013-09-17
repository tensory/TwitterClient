package net.tensory.apps.icantbelieveitsnottwitter;

import net.tensory.apps.icantbelieveitsnottwitter.models.User;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ProfileImageOnClickListener implements OnClickListener {
	User user;
	
	public ProfileImageOnClickListener(User u) {
		user = u;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("IMAGE_USER", user.getScreenName());
	}
}
