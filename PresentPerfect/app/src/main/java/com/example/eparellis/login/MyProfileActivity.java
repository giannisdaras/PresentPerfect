package com.example.eparellis.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MyProfileActivity extends AppCompatActivity {

    TextView txtUserId,txtName,txtNotes;
    private String UserId,InitialUser;


    private String txtUserIdV, txtNotesV, txtNameV; //sap

    // sap final Button button2=(Button)findViewById(R.id.button2);


    private static final String TAG_PRODUCT = "profile";
    private static final String TAG_NAME = "name";
    private static final String TAG_NOTES = "notes";
    private static final String TAG_SUCCESS="success";
    private static final String url_product_details = "http://strathpar.com/pp/get_profile_details.php";

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //Button button2=(Button)findViewById(R.id.button2);
        Button button3=(Button)findViewById(R.id.button3);
        Button btnadd=(Button)findViewById(R.id.btnNewAdd);
        final TextView txtName = (TextView) findViewById(R.id.nametext); // sap
        final TextView txtUserId = (TextView) findViewById(R.id.usertext);
        final TextView txtNotes = (TextView) findViewById(R.id.notestext);
        //sap
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();
        UserId = intentBundle.getString("USERNAME");
        InitialUser = intentBundle.getString("LOGGEDUSERID");

        Log.i("userId=", UserId); //sap

        // Getting complete product details in background thread
        new GetProductDetails().execute();
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i2 = new Intent(MyProfileActivity.this, ViewWishesActivity.class);


                i2.putExtra("USER", UserId);

                startActivity(i2);
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i5 = new Intent(MyProfileActivity.this,NewFriendsActivity.class);


                i5.putExtra("USER", UserId);
                i5.putExtra("LOGGEDUSERID",InitialUser);
                startActivity(i5);
            }
        });
       /* button2.setOnClickListener(

                new Button.OnClickListener() {

                    public void onClick(View v) {


                        Intent i = new Intent(MyProfileActivity.this, LoginActivity.class);


                        TextView txtName1 = (TextView) findViewById(R.id.nametext); // sap
                        TextView txtUserId1 = (TextView) findViewById(R.id.usertext);
                        TextView txtNotes1 = (TextView) findViewById(R.id.notestext);


                        i.putExtra("USER", UserId);
                        i.putExtra("PASSWORD", txtNotes.getText().toString());
                        i.putExtra("NAME", txtName.getText().toString());


                        //sap startActivity(i);
                        startActivityForResult(i, 100); //sap

                    }
                }
        ); */




        /* sap
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();
         UserId = intentBundle.getString("USERNAME");

        Log.i("userId=", UserId); //sap

        // Getting complete product details in background thread
        new GetProductDetails().execute();
        */


    }

    //sap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }


    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MyProfileActivity.this);
            pDialog.setMessage("Loading profile details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            //runOnUiThread(new Runnable() {
            //@Override
            //public void run() {
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("UserId", UserId));


                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_product_details, "GET", params1);

                // check your log for json response
                Log.d("Single Product Details", json.toString() + "#" + UserId);

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray(TAG_PRODUCT); // JSON Array

                    // get first product object from JSON Array
                    JSONObject product = productObj.getJSONObject(0);

                    // product with this pid found
                            /* sap
                            txtName = (TextView) findViewById(R.id.nametext2);
                            txtUserId = (TextView) findViewById(R.id.usertext2);
                            txtNotes = (TextView) findViewById(R.id.notestext2);
                            */
                            /* sap
                            txtName = (TextView) findViewById(R.id.nametext); // sap
                            txtUserId = (TextView) findViewById(R.id.usertext);
                            txtNotes = (TextView) findViewById(R.id.notestext);
                            */
                            /*
                            // display product data in EditText
                            txtName.setText(product.getString("Name"));
                            txtNotes.setText(product.getString("Password"));
                            txtUserId.setText(product.getString("UserId"));
                            */

                    txtNameV = product.getString("Name"); //sap
                    txtNotesV = product.getString("Password");
                    txtUserIdV = product.getString("UserId");

                    Log.i("Vfields", txtNameV);

                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //}
            //});

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.cancel();
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    // updating view
                    Log.i("abc", "Orxis" + txtUserIdV); //sap
                    TextView txtName = (TextView) findViewById(R.id.nametext); // sap
                    TextView txtUserId = (TextView) findViewById(R.id.usertext);
                    TextView txtNotes = (TextView) findViewById(R.id.notestext);
                    txtName.setText(txtNameV); //sap
                    //txtNotes.setText(txtNotesV);
                    txtUserId.setText(txtUserIdV);
                }
            });
        }
    }


}
