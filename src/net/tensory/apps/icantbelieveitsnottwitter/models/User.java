package net.tensory.apps.icantbelieveitsnottwitter.models;

import org.json.JSONObject;
import com.activeandroid.*;
import com.activeandroid.annotation.*;

@Table(name = "User")
public class User extends Model {
	@Column(name = "name")
	private String name;
	@Column(name = "screen_name")
	private String screenName;
	@Column(name = "profile_image_url_https")
	private String profileImageUrlHttps;
	
    public String getName() {
        return this.name;
    }
    
    public String getScreenName() {
        return this.screenName;
    }
    
    public String getProfileImageUrl() {
        return this.profileImageUrlHttps;
    }
    
/*
    public long getId() {
        return 1;
    }

    

    public String getProfileImageUrl() {
        return getString("profile_image_url");
    }

    public String getProfileBackgroundImageUrl() {
        return getString("profile_background_image_url");
    }

    public int getNumTweets() {
        return getInt("statuses_count");
    }

    public int getFollowersCount() {
        return getInt("followers_count");
    }

    public int getFriendsCount() {
        return getInt("friends_count");
    }
*/
    public static User fromJson(JSONObject json) {
        User u = new User();
        
        try {
        	u.name = json.getString("name");
        	u.screenName = json.getString("screen_name");
        	u.profileImageUrlHttps = json.getString("profile_image_url_https");
        	u.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }
}