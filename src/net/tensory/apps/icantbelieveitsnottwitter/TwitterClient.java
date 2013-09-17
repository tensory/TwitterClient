package net.tensory.apps.icantbelieveitsnottwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "79NTG7DaLNCpTMnZ5abNPQ";       // Change this
    public static final String REST_CONSUMER_SECRET = "8ss5z6LZQCgFUBuIxSscHWTdqqNDQlRPBN4H8PWFzo"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://icantbelieveitsnottwitter"; // Change this (here and in manifest)
    
    private static final String HOME_TIMELINE_URL = "statuses/home_timeline.json";
    private static final String MENTIONS_URL = "statuses/mentions_timeline.json";
    private static final String VERIFY_CREDENTIALS_URL = "account/verify_credentials.json";
    private static final String SHOW_USER_URL = "users/show.json";
    
    
	public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl(HOME_TIMELINE_URL);
    	client.get(url, null, handler);
    }
    
    public void getHomeTimeline(AsyncHttpResponseHandler handler, long oldestTweetId) {
    	// Get tweets older than the specified oldestTweetId 
    	// that belong in this timeline
    	String url = getApiUrl(HOME_TIMELINE_URL);
    	RequestParams params = new RequestParams();
    	params.put("max_id", String.valueOf(oldestTweetId));
    	client.get(url, params, handler);
    }
    
    public void getMentions(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl(MENTIONS_URL);
    	client.get(url, null, handler);
    }
    
    public void getMentions(AsyncHttpResponseHandler handler, long oldestTweetId) {
    	String url = getApiUrl(MENTIONS_URL);
    	RequestParams params = new RequestParams();
    	params.put("max_id", String.valueOf(oldestTweetId));
    	client.get(url, params, handler);
    }
    
    public void getUserInfo(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl(VERIFY_CREDENTIALS_URL);
    	client.get(url, null, handler);
    }
    
    public void getUserInfo(AsyncHttpResponseHandler handler, String screenName) {
    	String url = getApiUrl(SHOW_USER_URL);
    	RequestParams params = new RequestParams();
    	params.put("screen_name", screenName);
    	client.get(url, params, handler);
    }
    
    public void postTweet(String content, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", content);
    	client.post(url, params, handler);
    }
    
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}