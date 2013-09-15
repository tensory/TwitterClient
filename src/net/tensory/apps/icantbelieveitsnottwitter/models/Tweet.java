package net.tensory.apps.icantbelieveitsnottwitter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.*;
import com.activeandroid.query.*;
import com.activeandroid.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "Tweets")
public class Tweet extends Model {
	@Column(name="user")
	private User user;
    @Column(name = "text")
    private String text;
    @Column(name="created_at")
    private String createdAt;
    
    public User getUser() {
        return this.user;
    }

    public String getBody() {
        return this.text;
    }
    
    public String getTimestamp() {
    	return this.createdAt;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.text = jsonObject.getString("text");
            tweet.createdAt = jsonObject.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            tweet.save();
            if (tweet != null) {
                tweets.add(tweet);
            }
        }
        return tweets;
    }
    
    public static ArrayList<Tweet> getAll() {
    	List<Tweet> tw = new Select().from(Tweet.class)
    	.execute();
    	return (ArrayList<Tweet>) tw;
    }
}