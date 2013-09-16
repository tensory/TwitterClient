package net.tensory.apps.icantbelieveitsnottwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TimelineActivity extends FragmentActivity {
	public static final int COMPOSE_ACTIVITY_ID = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);		
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
	
	private void startComposeAction() {
		Intent i = new Intent(getBaseContext(), ComposeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(i, COMPOSE_ACTIVITY_ID);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
	          Intent data) {
	      if (requestCode == COMPOSE_ACTIVITY_ID) {
	          if (resultCode == RESULT_OK) {
	        	  Toast.makeText(getApplicationContext(), "Fix your closing handler for Compose", Toast.LENGTH_LONG).show();
	        	  /*
	      		TwitterClientApp.getRestClient().getHomeTimeline(new TweetsListFragment.TimelineJsonHttpResponseHandler() {
					
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						// TODO Auto-generated method stub
						
					}
				};TimelineJsonHttpResponseHandler(getBaseContext()));
				*/
	          }
	      }
	}
}
