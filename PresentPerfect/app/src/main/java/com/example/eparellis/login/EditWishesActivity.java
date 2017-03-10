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

public class EditWishesActivity extends Activity {

    TextView txtWishNumber;
    EditText txtDescription;
    String WishNumber;

    Button btnSave;
    Button btnDelete;

    private String txtDescriptionV, txtWishNumberV, DV, NV; //sap

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_product_details = "http://strathpar.com/pp/get_wishes_details.php";

    // url to update product
    private static final String url_update_product = "http://strathpar.com/pp/update_wishes.php";

    // url to delete product
    private static final String url_delete_product = "http://strathpar.com/pp/delete_wishes.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "wishes";
    private static final String TAG_PID = "WishNumber";
    private static final String TAG_NAME = "Description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_wishes);

        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        final EditText txtDescription = (EditText) findViewById(R.id.Description);
        final TextView txtWishNumber = (TextView) findViewById(R.id.WishNumber);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        WishNumber = i.getStringExtra(TAG_PID);

        Log.i("WNxxxx", WishNumber);

        // Getting complete product details in background thread
        new GetProductDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                DV = txtDescription.getText().toString();
                NV = WishNumber;

                Log.i("DVNV", NV + " " + "DV");

                new SaveProductDetails().execute();
            }
        });

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
            pDialog = new ProgressDialog(EditWishesActivity.this);
            pDialog.setMessage("Loading wish details. Please wait...");
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
                params1.add(new BasicNameValuePair("WishNumber", WishNumber));



                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_product_details, "GET", params1);

                // check your log for json response
                Log.d("Single Wish Details", json.toString());

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

                    txtDescriptionV = product.getString("Description");
                    txtWishNumberV = product.getString("WishNumber");

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
                    EditText txtDescriptionA = (EditText) findViewById(R.id.Description);
                    TextView txtWishNumberA = (TextView) findViewById(R.id.WishNumber);
                    txtDescriptionA.setText(txtDescriptionV);
                    txtWishNumberA.setText(txtWishNumberV);
                }
            });
        }
    }

    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditWishesActivity.this);
            pDialog.setMessage("Saving wish ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String wishnumber = NV;
            String description = DV;


            //sap
            Log.i("info", wishnumber + " " + description);
            //end sap

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("WishNumber", wishnumber));
            params.add(new BasicNameValuePair("Description", description));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);

            //sap
            Log.d("update result", json.toString());
            //end sap

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
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
            // dismiss the dialog once product uupdated
            pDialog.dismiss();

        }
    }

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
            pDialog = new ProgressDialog(EditWishesActivity.this);
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
                Log.i("info", WishNumber);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("WishNumber",WishNumber ));

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


