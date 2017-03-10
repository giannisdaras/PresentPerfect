package com.example.eparellis.login;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewFWishesActivity extends ListActivity {
    private String UserId, FriendId;
    private String WN;
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    private static String url_all_products = "http://strathpar.com/pp/get_wishes_of_user.php";
    private static String url_donate = "http://strathpar.com/pp/donate.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "wishes";
    private static final String TAG_PID = "WishNumber";
    private static final String TAG_NAME = "Description";
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    // products JSONArray
    JSONArray wishes = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Toast toast = Toast.makeText(getApplicationContext(), "Click on a a wish to reserve it. But be careful, because promises are made to be kept!", Toast.LENGTH_LONG);
        toast.show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_fwishes);
        Intent i = getIntent();

        // getting product id (pid) from intent
        UserId = i.getStringExtra("USER");
        FriendId = i.getStringExtra("FRIEND");
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllProducts().execute();

        // Get listview
        ListView lv = getListView();


        // on seleting single record
        // launching Edit Wish Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem

                //Log.i("info", "Item Clicked");

                String WishNumber = ((TextView) view.findViewById(R.id.WishNumber)).getText().toString();
                String DonatorId = ((TextView) view.findViewById(R.id.DonatorId)).getText().toString();

                Log.i("Donator", "Don= " + DonatorId);

                if (DonatorId == null || DonatorId.isEmpty() || DonatorId == "null" ) {

                    /*
                    Intent in = new Intent(getApplicationContext(),
                            EditWishesActivity.class);
                    // sending pid to next activity
                    in.putExtra(TAG_PID, WishNumber);

                    Log.i("info", WishNumber.toString()); //sap
                    // starting new activity and expecting some response back
                    startActivityForResult(in, 100);
                    */
                    WN = WishNumber;
                    new donate().execute();

                } else {
                    Context context=getApplicationContext();
                    CharSequence text="Wish already donated";
                    int duration= Toast.LENGTH_LONG;
                    Toast toast= Toast.makeText(context, text, duration);
                    toast.show();

                }

            }
        });


    }

    // Response from Edit Product Activity
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
     * Background Async Task to Load all product by making HTTP Request
     * */

    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewFWishesActivity.this);
            pDialog.setMessage("Loading wishes. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserId", FriendId));
            // getting JSON string from URL

            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Wishes: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    wishes = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i <wishes.length(); i++) {
                        JSONObject c = wishes.getJSONObject(i);

                        Log.d("rrrr", c.toString());

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String donatorid = c.getString("DonatorId");

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put("DonatorId", donatorid );

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                } else {
                    // no wishes found

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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ViewFWishesActivity.this, productsList,
                            R.layout.list_fmain, new String[] {TAG_PID,
                            TAG_NAME, "DonatorId"},
                            new int[] { R.id.WishNumber,R.id.Description, R.id.DonatorId });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

    class donate extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewFWishesActivity.this);
            pDialog.setMessage("Donating ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("WishNumber", WN));
            params.add(new BasicNameValuePair("UserId", UserId));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_donate,
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
    }}


