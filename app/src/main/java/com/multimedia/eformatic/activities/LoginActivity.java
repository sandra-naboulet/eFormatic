package com.multimedia.eformatic.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.multimedia.eformatic.R;
import com.multimedia.eformatic.managers.CreditsManager;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button mLoginButton;
    private LoginButton mFBLoginButton;

    private CallbackManager mCallbackManager;
    private Handler mHandler;

    private boolean mIsLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);

        mFBLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("FB", "success");
                mIsLogged = true;
            }

            @Override
            public void onCancel() {
                Log.e("FB", "cancel");
                mIsLogged = false;

            }

            @Override
            public void onError(FacebookException e) {
                Log.e("FB", "error");
                mIsLogged = false;

            }
        });


        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        login();
                    default:
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

        if (mHandler != null) {
            if (mIsLogged) {
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    private void updateDisplay() {


    }

    private void login() {
        // Init credits
        CreditsManager.getInstance().saveCredits(15);

        mIsLogged = false;
        // Go to main activity
        Intent intent = new Intent(LoginActivity.this, CategoriesActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button:
                login();
                break;
        }
    }
}
