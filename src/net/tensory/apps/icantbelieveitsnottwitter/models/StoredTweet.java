package net.tensory.apps.icantbelieveitsnottwitter.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.activeandroid.*;
import com.activeandroid.annotation.*;
import com.activeandroid.query.Select;
import android.util.Log;

@Table(name = "StoredTweets")
public class StoredTweet extends Model {
	@Column(name = "name")
    public String name;
	@Column(name = "screen_name")
    public String screen_name;
	@Column(name = "profile_image_url_https")
    public String profile_image_url_https;
	@Column(name = "created_at")
    public String created_at;
	@Column(name = "text")
    public String text;
	
    public StoredTweet() {
    	super();
    }
    
    public StoredTweet(JSONObject tweet) {
    	super();
    	try {
			this.name = tweet.getString("name");
			this.screen_name = tweet.getString("screen_name");
	    	this.profile_image_url_https = tweet.getString(profile_image_url_https);
	    	this.text = tweet.getString("text");
	    	this.created_at = tweet.getString("created_at");
	    	Log.d("STORING", this.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static ArrayList<StoredTweet> getAll() {
    	return new Select()
    	.from(StoredTweet.class)
    	.execute();
    }
}