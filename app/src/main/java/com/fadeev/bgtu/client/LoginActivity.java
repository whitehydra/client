package com.fadeev.bgtu.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fadeev.bgtu.client.dto.AuthorizationDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;


import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    String TAG = "Login activity";

    private UserLoginTask userLoginTask = null;
    private EditText username;
    private EditText password;
    private View progress;
    private View loginForm;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.lgUsername);
        password = (EditText) findViewById(R.id.lgPassword);
        loginForm = findViewById(R.id.lgLoginFormScroll);
        progress = findViewById(R.id.login_progress);

        Button mEmailSignInButton = (Button) findViewById(R.id.lgSignInButton);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        if (userLoginTask != null) { return; }
        username.setError(null);
        password.setError(null);

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            this.username.setError(getString(R.string.error_empty_username));
            focusView = this.username;
            cancel = true;
        }
        if(TextUtils.isEmpty(password)) {
            this.password.setError(getString(R.string.error_empty_password));
            focusView = this.password;
            cancel = true;
        }else if (!isPasswordValid(password)) {
            this.password.setError(getString(R.string.error_invalid_password));
            focusView = this.password;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            userLoginTask = new UserLoginTask(username, password,this);
            userLoginTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        loginForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        progress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }




    public class UserLoginTask extends AsyncTask<Void, Void, String> {
        private final String mUsername;
        private final String mPassword;
        private final Context mContext;

        UserLoginTask(String email, String password, Context context) {
            mUsername = email;
            mPassword = password;
            mContext = context;
        }
        @Override
        protected String doInBackground(Void... params) {
            AuthorizationDTO authorizationDTO = new AuthorizationDTO();
            authorizationDTO.setUsername(mUsername);
            try {
                authorizationDTO.setPassword(Functions.generateHash(mPassword));
            } catch (Exception e) { e.printStackTrace(); }

            Call<String> call = NetworkService.getInstance().getJSONApi().postAuthorizationGetToken(authorizationDTO);
            try { return call.execute().body(); } catch (Exception e) { return ""; }
        }
        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String token) {
            token = token.replace("\"","");
            userLoginTask = null;
            showProgress(false);

            if (!token.equals("")) {
                SharedPreferences sPref = mContext.getSharedPreferences(Constants.PREFERENCES.MAIN,MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(Constants.PREFERENCES.USERNAME,mUsername);
                ed.putString(Constants.PREFERENCES.TOKEN,token);
                ed.commit();

                Intent intent = new Intent(LoginActivity.this, PINActivity.class);
                startActivity(intent);
                finish();
            } else {
                password.setError(getString(R.string.error_incorrect_connection));
                password.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            userLoginTask = null;
            showProgress(false);
        }
    }
}

