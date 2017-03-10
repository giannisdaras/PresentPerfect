package com.example.eparellis.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewFriendsActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    TextView iUserId;
    EditText iFriendId;
    String UserId,InitialUser;


    // url to create new product
    private static String url_create_friends = "http://strathpar.com/pp/create_friends.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_friends);

        Intent i = getIntent();
        UserId = i.getStringExtra("USER");
        InitialUser=i.getStringExtra("LOGGEDUSERID");

        // Edit Text
        iUserId = (TextView) findViewById(R.id.UserId);
        iFriendId = (EditText) findViewById(R.id.FriendUserId);

        iFriendId.setText(UserId);
        iUserId.setText(InitialUser);

        // Create button
        Button btnCreateFriend = (Button) findViewById(R.id.btnCreateFriend);

        // button click event
        btnCreateFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewFriend().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewFriend extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewFriendsActivity.this);
            pDialog.setMessage("Creating Friend..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String UserId = iUserId.getText().toString();
            String FriendUserId = iFriendId.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserId", UserId));
            params.add(new BasicNameValuePair("FriendId", FriendUserId));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_friends,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created friend
                    // TODO Intent i = new Intent(getApplicationContext(), ViewFriendsActivity.class);
                    //i.putExtra("USER", UserId);
                    //startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}

