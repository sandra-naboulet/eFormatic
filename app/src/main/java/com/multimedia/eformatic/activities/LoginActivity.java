package com.multimedia.eformatic.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.multimedia.eformatic.R;
import com.multimedia.eformatic.managers.CreditsManager;
import com.multimedia.eformatic.managers.HistoryManager;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button mLoginButton;
    private Button mLoginTitleButton;
    private Button mRegisterTitleButton;

    private boolean mIsLoginView = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
       // mLoginTitleButton = (Button) findViewById(R.id.login_title);
        //mLoginTitleButton.setOnClickListener(this);
        //mRegisterTitleButton = (Button) findViewById(R.id.register_title);
        //mRegisterTitleButton.setOnClickListener(this);

    }


    private void updateDisplay() {


    }

    private void login() {
        // Init credits
        CreditsManager.getInstance().saveCredits(3);

        // Go to main activity
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.login_title:
                mIsLoginView = true;
                updateDisplay();
                break;
            case R.id.register_title:
                mIsLoginView = false;
                updateDisplay();
                break;*/
            case R.id.login_button:
                login();
                break;
        }
    }
}
