package net.tensory.apps.icantbelieveitsnottwitter;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;

public class ComposeActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
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
}
