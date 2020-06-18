package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappclone.MainActivity;
import com.example.whatsappclone.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginEmail,edtLoginPassword;
    private Button btnLoginActivity,btnSignUpLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() { // THIS IS PUT TO THE PASSWORD BECAUSE THE LAST THING THAT USER PUT WHEN SIGNING IS PASSWORD...
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    onClick(btnLoginActivity); // this is because of the button view. it is a sub class of the view class..

                }
                return false;
            }

        });
        btnLoginActivity = findViewById(R.id.btnLoginLogin);
        btnSignUpLoginActivity= findViewById(R.id.btnLoginSignUp);

        btnLoginActivity.setOnClickListener(this);
        btnSignUpLoginActivity.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();// this let the user to sign up whenever the app runs.so as to log out the user that already signed up or Logged in.
        }



    }

    @Override
    public void onClick(View buttonView) {
        switch (buttonView.getId()){

            case R.id.btnLoginLogin:
                ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if(user != null && e == null){

                            FancyToast.makeText(LoginActivity.this, user.getUsername() + " is logged in successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            transitionToSocialMediaActivity();

                        }else{

                            FancyToast.makeText(LoginActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                        }

                    }
                });


                break;

            case R.id.btnLoginSignUp:

                Intent intent = new Intent(LoginActivity.this, MainActivity.class); // this must be put in oder to move in to the next activity window...
                startActivity(intent);
                ParseUser.getCurrentUser().logOut();

                break;

        }

    }

    public void rootLayoutClicked(View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {

            e.printStackTrace();// it is going to output some values to the log.

        }

    }

    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(LoginActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();

    }
}