package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class composeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;
    private EditText etCompose;
    private Button btnTweet;
    private TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        client = TwitterApp.getRestClient(this);
        btnTweet.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(composeActivity.this, "Cannot sent empty tweet", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tweetContent.length() > MAX_TWEET_LENGTH ){
                    Toast.makeText(composeActivity.this, "Tweet too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Toast.makeText(composeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                client.composeTweet(tweetContent, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Toast.makeText(composeActivity.this, "yo", Toast.LENGTH_LONG).show();
                        try {
                            Tweet tweet = Tweet.fromJson(response);
                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, data);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("TwitterClient", "Failed to post tweet" + errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("TwitterClient", "Failed to post tweet" + errorResponse);

                    }
                });

            }
        });
    }
}
