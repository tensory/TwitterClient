package net.tensory.apps.icantbelieveitsnottwitter;

import net.tensory.apps.icantbelieveitsnottwitter.fragments.HomeTimelineFragment;
import net.tensory.apps.icantbelieveitsnottwitter.fragments.MentionsFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TimelineActivity extends FragmentActivity implements TabListener {
	public static final int PROFILE_ACTIVITY_ID = 1;
	public static final int COMPOSE_ACTIVITY_ID = 2;
	private static final String HOME_TAG = "HomeTimelineFragment";
	private static final String MENTIONS_TAG = "MentionsFragment";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);	
		setupNavigationTabs();
	}

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab().setText(getResources().getString(R.string.tab_home))
				.setTag(HOME_TAG)
				.setTabListener(this)
				.setIcon(R.drawable.ic_home);
		
		Tab tabMentions = actionBar.newTab().setText(getResources().getString(R.string.tab_mentions))
				.setTag(MENTIONS_TAG)
				.setTabListener(this)
				.setIcon(R.drawable.ic_mentions);
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
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
			startActionWithoutAnimation(ComposeActivity.class, COMPOSE_ACTIVITY_ID);
	      break;
		case R.id.action_profile:
			startActionWithoutAnimation(ProfileActivity.class, PROFILE_ACTIVITY_ID);
	    default:
	    	break;
	    }
	    return true;
	  }
	
	private void startActionWithoutAnimation(Class targetClass, int resultId) {
		Intent i = new Intent(getBaseContext(), targetClass);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(i, resultId);
	}
/*
	protected void onActivityResult(int requestCode, int resultCode,
	          Intent data) {
	      if (requestCode == COMPOSE_ACTIVITY_ID) {
	          if (resultCode == RESULT_OK) {
	        	  FragmentManager manager = getSupportFragmentManager();
	        	  android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
	        	  fts.replace(R.id.frameLayout, new HomeTimelineFragment());
	        	  fts.commit();
	          }
	      }
	}
*/
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if (tab.getTag() == HOME_TAG) {
			// Set the fragment in frameLayout to use the HomeTimelineFragment
			fts.replace(R.id.frameLayout, new HomeTimelineFragment());
		} else if (tab.getTag() == MENTIONS_TAG) {
			fts.replace(R.id.frameLayout, new MentionsFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
}
