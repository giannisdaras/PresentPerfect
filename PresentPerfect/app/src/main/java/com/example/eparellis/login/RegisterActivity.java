package com.example.eparellis.login;


        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {
    protected EditText username;
    private EditText password;
    private EditText name;
    protected String enteredUsername, enteredPassword, enteredName;
    private final String serverUrl = "http://strathpar.com/pp/index.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText)findViewById(R.id.username_field);
        password = (EditText)findViewById(R.id.password_field);
        name = (EditText)findViewById(R.id.name_field);
        Button signUpButton = (Button)findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredUsername = username.getText().toString();
                enteredPassword = password.getText().toString();
                enteredName = name.getText().toString();
                if(enteredUsername.equals("") || enteredPassword.equals("") || enteredName.equals("")){
                    Toast.makeText(RegisterActivity.this, "Username or password or email must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if(enteredUsername.length() <= 1 || enteredPassword.length() <= 1){
                    Toast.makeText(RegisterActivity.this, "Username or password length must be greater than one", Toast.LENGTH_LONG).show();
                    return;
                }
// request authentication with remote server4
                AsyncDataClass asyncRequestObject = new AsyncDataClass();
                asyncRequestObject.execute(serverUrl, enteredUsername, enteredPassword, enteredName);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class AsyncDataClass extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            // sap HttpPost httpPost = new HttpPost(params[0]);
            HttpPost httpPost = new HttpPost(serverUrl);
            String jsonResult = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("UserId",  enteredUsername));
                nameValuePairs.add(new BasicNameValuePair("Password", enteredPassword));
                nameValuePairs.add(new BasicNameValuePair("Name", enteredName));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("jryyy", jsonResult); //sap
            return jsonResult;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if(result.equals("") || result == null){
                Toast.makeText(RegisterActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if(jsonResult == 0){
                Toast.makeText(RegisterActivity.this, "Invalid username or password or name", Toast.LENGTH_LONG).show();
                return;
            }
            if(jsonResult == 1){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("USERNAME", enteredUsername);
                intent.putExtra("MESSAGE", "You have been successfully Registered");
                startActivity(intent);
            }
        }
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }
    private int returnParsedJsonObject(String result){
        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
}
