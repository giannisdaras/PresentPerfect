package com.example.eparellis.login;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class ViewFriendsActivity extends ListActivity {
    private String UserId, FriendId;
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    private static String url_all_products = "http://strathpar.com/pp/get_friends_of_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "friends";
    private static final String TAG_PID = "UserId";
    private static final String TAG_NAME = "FriendId";
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    // products JSONArray
    JSONArray wishes = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Toast toast = Toast.makeText(getApplicationContext(), "Click on a friend to view profile and wishlist", Toast.LENGTH_LONG);
        toast.show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_friends);
        Intent i = getIntent();

        // getting product id (pid) from intent
        UserId = i.getStringExtra("USER");

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

                String UserId = ((TextView) view.findViewById(R.id.UserId)).getText().toString();
                String FriendId = ((TextView) view.findViewById(R.id.FriendId)).getText().toString();

                Intent in = new Intent(getApplicationContext(),
                        EditFriendsActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, UserId);
                in.putExtra(TAG_NAME, FriendId);

                Log.i("info", UserId.toString()); //sap
                // starting new activity and expecting some response back
                startActivityForResult(in, 100);


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
            pDialog = new ProgressDialog(ViewFriendsActivity.this);
            pDialog.setMessage("Loading friends. Please wait...");
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
            params.add(new BasicNameValuePair("UserId", UserId));
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

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);

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
                            ViewFriendsActivity.this, productsList,
                            R.layout.list_main2, new String[] {TAG_PID,
                            TAG_NAME},
                            new int[] { R.id.UserId,R.id.FriendId });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }}

