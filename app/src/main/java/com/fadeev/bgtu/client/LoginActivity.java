package com.fadeev.bgtu.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fadeev.bgtu.client.dto.AuthorizationDTO;
import com.fadeev.bgtu.client.file.OpenFileDialog;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {

    private UserLoginTask userLoginTask = null;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsernameView = (EditText) findViewById(R.id.lgUsername);
        mPasswordView = (EditText) findViewById(R.id.lgPassword);
        mLoginFormView = findViewById(R.id.lgLoginFormScroll);
        mProgressView = findViewById(R.id.login_progress);

        Button mEmailSignInButton = (Button) findViewById(R.id.lgSignInButton);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }




    private void attemptLogin() {
        if (userLoginTask != null) {
            return;
        }

        mUsernameView.setError(null);
        mPasswordView.setError(null);

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_empty_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
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

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void test (View v){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    Drawable image;



    public void openFile(View view){
        OpenFileDialog openFileDialog = new OpenFileDialog(this)
                .setFilter(".*\\.jpg")
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String fileName) {
                        Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
                        image = Drawable.createFromPath(fileName);

                        SendFileTask sendFileTask = new SendFileTask(fileName);
                        sendFileTask.execute((Void)null);

                    }
                });
        openFileDialog.show();
    }




    public class SendFileTask extends AsyncTask<Void, Void, String>{
        private final String fileName;

        SendFileTask(String fileName){
            this.fileName = fileName;
        }

        @Override
        protected String doInBackground(Void... voids) {
            //*********************
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
           Log.d("File", "result = " + result);
        }
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
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_connection));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
            showProgress(false);
        }
    }
}

