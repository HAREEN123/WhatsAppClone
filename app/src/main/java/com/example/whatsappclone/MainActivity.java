package com.example.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappclone.LoginActivity;
import com.example.whatsappclone.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword, edtUserName;
    private Button btnSignUp, btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up");// to change the name of the action bar.
        edtEmail = findViewById(R.id.edtEmail);  // this is for the Main Activity..
        edtPassword = findViewById(R.id.edtPassword);
        edtPassword.setOnKeyListener(new View.OnKeyListener() { // THIS IS PUT TO THE PASSWORD BECAUSE THE LAST THING THAT USER PUT WHEN SIGNING IS PASSWORD...
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    onClick(btnSignUp); // this is because of the button view. it is a sub class of the view class..

                }
                return false;
            }

        });
        edtUserName = findViewById(R.id.edtUserName);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this); //Remember THis Must be Within the On create Method..

        if (ParseUser.getCurrentUser() != null) { //  this means the user signed up or logged in . now we have the social media activity.
            //ParseUser.getCurrentUser().logOut();// this let the user to sign up whenever the app runs.so as to log out the user that already signed up or Logged in.
            transitionToSocialMediaActivity();
        }


    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()) {

            case R.id.btnSignUp:
                final ParseUser appUser = new ParseUser();
                appUser.setEmail(edtEmail.getText().toString());
                appUser.setUsername(edtUserName.getText().toString());
                appUser.setPassword(edtPassword.getText().toString());
                if (edtEmail.getText().toString().equals("") || edtUserName.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
                    // For Bux - Requiring all fields to be entered...!!
                    FancyToast.makeText(MainActivity.this, "Email, Username, Password is required!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();

                } else {

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up " + edtUserName.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {// Signing up to the server.
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                FancyToast.makeText(MainActivity.this, appUser.get("username") + " is signed up successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                transitionToSocialMediaActivity();


                            } else {
                                FancyToast.makeText(MainActivity.this, "There was an Error " + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }

                            progressDialog.dismiss(); // we do not need to show the progress bar when signed up.

                        }
                    });

                }

                break;
            case R.id.btnLogIn:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class); // this must be put in oder to move in to the next activity window...
                startActivity(intent);
                finish();

                break;


        }

    }

    public void layoutTapped(View view) {
        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {

            e.printStackTrace();// it is going to output some values to the log.

        }
    }

    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(MainActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}