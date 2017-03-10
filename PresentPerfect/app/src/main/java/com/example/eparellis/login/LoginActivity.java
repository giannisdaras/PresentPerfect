package com.example.eparellis.login;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();
        String loggedUser = intentBundle.getString("USERNAME");
        loggedUser = capitalizeFirstCharacter(loggedUser);
        String message = intentBundle.getString("MESSAGE");
        final TextView loginUsername = (TextView)findViewById(R.id.login_user);
        TextView successMessage = (TextView)findViewById(R.id.message);
        Button search_Button=(Button)findViewById(R.id.search_button);
        Button btnEditProfile=(Button)findViewById(R.id.btnProfileEdit);
        Button btnMy= (Button)findViewById(R.id.btnViewMyW);
        Button btnAdd= (Button)findViewById(R.id.btnNewWish);
        Button btnF= (Button)findViewById(R.id.btnFriends);
        Button btnD=(Button)findViewById(R.id.btnDonate);
        ImageButton image = (ImageButton)findViewById(R.id.imagebtn);


        final String finalLoggedUser3 = loggedUser;
        final String finalLoggedUser5 = loggedUser;
        btnD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent begin= new Intent(getApplicationContext(),MyDonationsActivity.class);
                begin.putExtra("USER", finalLoggedUser5);
                // begin.putExtra("NAME", String.valueOf(loginUsername));

                startActivityForResult(begin, 100);
            }
        });
        btnF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i6 = new Intent(LoginActivity.this,ViewFriendsActivity.class);


                i6.putExtra("USER", finalLoggedUser3);
                startActivity(i6);
            }
        });
        final String finalLoggedUser2 = loggedUser;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i4 = new Intent(getApplicationContext(), NewWishesActivity.class);
                i4.putExtra("USER", finalLoggedUser2);
                startActivity(i4);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (count==0) {
                    Toast toast = Toast.makeText(getApplicationContext(),"Advice 1: Add new friends by using the search menu!", Toast.LENGTH_LONG);
                    toast.show();
                    count++;
                }
                else if (count==1) {
                    Toast toast = Toast.makeText(getApplicationContext(),"Advice 2: Add new wishes, so your friends can always find the right present", Toast.LENGTH_LONG);
                    toast.show();
                    count=0;
                }

            }
        });

        final String finalLoggedUser1 = loggedUser;
        btnMy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i2 = new Intent(LoginActivity.this, ViewMyWishesActivity.class);


                i2.putExtra("USER", finalLoggedUser1);
                startActivity(i2);
            }
        });
        final String finalLoggedUser4 = loggedUser;
        search_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    Intent begin= new Intent(getApplicationContext(),SearchActivity.class);
                begin.putExtra("USER",finalLoggedUser4);
                startActivity(begin);
            }
        });
        final String finalLoggedUser = loggedUser;
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent begin= new Intent(getApplicationContext(),EditProfileActivity.class);
                begin.putExtra("USER", finalLoggedUser);
                // begin.putExtra("NAME", String.valueOf(loginUsername));

                startActivityForResult(begin, 100);
            }
        });
        loginUsername.setText(loggedUser);
        successMessage.setText(message);
        Toast toast = Toast.makeText(getApplicationContext(),"Click the icon for advices!", Toast.LENGTH_LONG);
        toast.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    private String capitalizeFirstCharacter(String textInput){
        String input = textInput.toLowerCase();
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }
}