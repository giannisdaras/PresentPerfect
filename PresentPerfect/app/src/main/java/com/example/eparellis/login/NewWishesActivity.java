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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewWishesActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    TextView iUserId;
    EditText iDescription;
    String UserId;


    // url to create new product
    private static String url_create_wishes = "http://strathpar.com/pp/create_wishes.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast toast = Toast.makeText(getApplicationContext(), "Create new wish and let the magic happen", Toast.LENGTH_LONG);
        toast.show();
        setContentView(R.layout.content_new_wishes);

        Intent i = getIntent();
        UserId = i.getStringExtra("USER");

        // Edit Text
        iUserId = (TextView) findViewById(R.id.UserId);
        iDescription = (EditText) findViewById(R.id.Description);

        iUserId.setText(UserId);

        // Create button
        Button btnCreateWish = (Button) findViewById(R.id.btnCreateWish);

        // button click event
        btnCreateWish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewWish().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewWish extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewWishesActivity.this);
            pDialog.setMessage("Creating Wish..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String UserId = iUserId.getText().toString();
            String Description = iDescription.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserId", UserId));
            params.add(new BasicNameValuePair("Description", Description));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_wishes,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ViewWishesActivity.class);
                    i.putExtra("USER", UserId);
                    startActivity(i);

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

