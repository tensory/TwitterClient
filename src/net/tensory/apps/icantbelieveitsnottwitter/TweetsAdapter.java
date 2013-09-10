package net.tensory.apps.icantbelieveitsnottwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;
import net.tensory.apps.icantbelieveitsnottwitter.TimelineActivity;
import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View view = convertView;
		    if (view == null) {
		    	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	view = inflater.inflate(R.layout.tweet_item, null);
		    }

	        Tweet tweet = getItem(position);
	        
	        ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
	        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);
	        
	        TextView nameView = (TextView) view.findViewById(R.id.tvName);
	        String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + " <small><font color='#777777'>@" +
	                tweet.getUser().getScreenName() + "</font></small>";
	        nameView.setText(Html.fromHtml(formattedName));
	        
	        TextView timestampView = (TextView) view.findViewById(R.id.tvTimestamp);
	        timestampView.setText(getFriendlyTimestamp(tweet.getTimestamp()));

	        TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
	        bodyView.setText(Html.fromHtml(tweet.getBody()));
	        
	        return view;
	}
	
	protected String getFriendlyTimestamp(String original) {
		Date date;
		String formatted = original;
		
    	// Assumes format from Twitter goes like: Thu Oct 21 16:02:46 +0000 2010
    	try {
			date = new SimpleDateFormat("EEE MMM d H:m:s ZZZ yyyy", Locale.ENGLISH).parse(original);
			long newTime = date.getTime();
			if (newTime >= System.currentTimeMillis()) {
				newTime = System.currentTimeMillis();
			}
	    	formatted = (String) DateUtils.getRelativeTimeSpanString(newTime, System.currentTimeMillis(), 0L);
    	} catch (ParseException e) {
    		Log.e("DATE ERROR", e.getStackTrace().toString());
		}
    	return formatted;
	}

}
