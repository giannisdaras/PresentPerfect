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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditFriendsActivity extends Activity {

    TextView txtUserId;
    TextView txtFriendId;
    TextView txtFName;
    TextView txtFNotes;
    String UserId, FriendId;

    Button btnViewFriendWishes;
    Button btnDelete;

    private String txtFriendIdV, txtUserIdV, txtFNameV, txtFNotesV, FV, UV; //sap

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_product_details = "http://strathpar.com/pp/get_friends_details.php";

    // url to update product
    //private static final String url_update_product = "http://strathpar.com/pp/update_wishes.php";

    // url to delete product
    private static final String url_delete_product = "http://strathpar.com/pp/delete_friends.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "friends";
    private static final String TAG_PID = "UserId";
    private static final String TAG_NAME = "FriendId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_friends);


        btnViewFriendWishes = (Button) findViewById(R.id.btnViewFriendWishes);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        final TextView txtFriendId = (TextView) findViewById(R.id.FriendId);
        final TextView txtUserId = (TextView) findViewById(R.id.UserId);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        UserId = i.getStringExtra(TAG_PID);
        FriendId = i.getStringExtra(TAG_NAME);

        Log.i("key1", "xxx" + UserId + FriendId);

        // Getting complete product details in background thread
        new GetProductDetails().execute();


        // View Friend Wishes button click event
        btnViewFriendWishes.setOnClickListener(

                new Button.OnClickListener() {

                    public void onClick(View v) {
                        Intent i2 = new Intent(EditFriendsActivity.this,ViewFWishesActivity.class);
                        i2.putExtra("USER", UserId);
                        i2.putExtra("FRIEND", FriendId);
                        startActivity(i2);
                    }
                }
        );

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteProduct().execute();

            }
        });

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
            pDialog = new ProgressDialog(EditFriendsActivity.this);
            pDialog.setMessage("Loading friend details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            // sap runOnUiThread(new Runnable() {
            // sap public void run() {
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("UserId", UserId));
                params1.add(new BasicNameValuePair("FriendId", FriendId));



                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_product_details, "GET", params1);

                // check your log for json response
                Log.d("Single Friend Details", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray(TAG_PRODUCT); // JSON Array

                    // get first product object from JSON Array
                    JSONObject product = productObj.getJSONObject(0);

                    // product with this pid found
                    // Edit Text
                            /*
                            txtDescription = (EditText) findViewById(R.id.Description);
                            txtWishNumber = (EditText) findViewById(R.id.WishNumber);
                            */

                    // display product data in EditText
                            /*
                            txtDescription.setText(product.getString("Description"));
                            txtWishNumber.setText(product.getString("WishNumber"));
                            */

                    txtFriendIdV = product.getString("FriendId");
                    txtUserIdV = product.getString("UserId");
                    txtFNameV = product.getString("FName");
                    txtFNotesV = product.getString("FNotes");

                }else{
                    // product with pid not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // sap}
            // sap });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    // display product data in EditText
                    TextView txtFriendIdA = (TextView) findViewById(R.id.FriendId);
                    //TextView txtUserIdA = (TextView) findViewById(R.id.UserId);
                    TextView txtFNameA = (TextView) findViewById(R.id.FName);
                    TextView txtFNotesA = (TextView) findViewById(R.id.FNotes);
                    txtFriendIdA.setText(txtFriendIdV);
                    //txtUserIdA.setText(txtUserIdV);
                    txtFNameA.setText(txtFNameV);
                    txtFNotesA.setText(txtFNotesV);
                }
            });
        }
    }

    /**
     * Background Async Task to  Save product Details
     * */



    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditFriendsActivity.this);
            pDialog.setMessage("Deleting Wish ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                Log.i("key", "xxx" + UserId + FriendId);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("UserId",UserId ));
                params.add(new BasicNameValuePair("FriendId",FriendId ));
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_product, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // wish successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
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
            // dismiss the dialog once product deleted
            pDialog.dismiss();

        }

    }
}


