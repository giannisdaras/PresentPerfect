package com.example.eparellis.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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




public class EditProfileActivity extends AppCompatActivity {
    EditText txtName,txtNotes;
    TextView txtUserId;

    private String UserId, Password, Name;
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    // url to update product
    private static final String url_update_product = "http://strathpar.com/pp/update_profile.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "profile";
    private static final String TAG_PID = "UserId";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        UserId = i.getStringExtra("USER");
        Password = i.getStringExtra("PASSWORD");
        Name = i.getStringExtra("NAME");

        //define edit texts
        txtName = (EditText) findViewById(R.id.nametext2); // sap
        txtUserId = (TextView) findViewById(R.id.usertext2);
        txtNotes = (EditText) findViewById(R.id.notestext2);
        txtUserId.setText(UserId);
        txtNotes.setText(Password);
        txtName.setText(Name);
        // save button click event
        final Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveProductDetails().execute();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    class SaveProductDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProfileActivity.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String userid = txtUserId.getText().toString();
            String name = txtName.getText().toString();
            String password = txtNotes.getText().toString();

            //sap
            Log.i("info", userid + " " + name + " " + password);
            //end sap

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserId", userid));
            params.add(new BasicNameValuePair("Name", name));
            params.add(new BasicNameValuePair("Password", password));
            params.add(new BasicNameValuePair("Notes", "Hi"));

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
        } } }



