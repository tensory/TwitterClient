package net.tensory.apps.icantbelieveitsnottwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.nostra13.universalimageloader.core.ImageLoader;
import net.tensory.apps.icantbelieveitsnottwitter.TweetsAdapter;
import net.tensory.apps.icantbelieveitsnottwitter.models.Tweet;
import net.tensory.apps.icantbelieveitsnottwitter.models.StoredTweet;
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

public class StoredTweetsAdapter extends ArrayAdapter<StoredTweet> {

	public StoredTweetsAdapter(Context context, List<StoredTweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View view = convertView;
		    if (view == null) {
		    	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	view = inflater.inflate(R.layout.tweet_item, null);
		    }

	        StoredTweet tweet = getItem(position);
	        
	        ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
	        ImageLoader.getInstance().displayImage(tweet.profile_image_url_https, imageView);
	        
	        TextView nameView = (TextView) view.findViewById(R.id.tvName);
	        String formattedName = "<b>" + tweet.name + "</b>" + " <small><font color='#777777'>@" +
	                tweet.screen_name + "</font></small>";
	        nameView.setText(Html.fromHtml(formattedName));
	        
	        TextView timestampView = (TextView) view.findViewById(R.id.tvTimestamp);
	        timestampView.setText(TweetsAdapter.getFriendlyTimestamp(tweet.created_at));

	        TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
	        bodyView.setText(Html.fromHtml(tweet.text));
	        
	        return view;
	}

}
