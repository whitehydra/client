package com.fadeev.bgtu.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.UserDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import retrofit2.Call;

public class SplashActivity extends AppCompatActivity {
    String TAG = "Splash activity";

    private AutoLoginTask autoLoginTask = null;
    private TextView textWelcome;
    private ConstraintLayout splashFrame;
    View progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.setLocale(this);
        setTheme(Functions.getSharedTheme(this));
        setContentView(R.layout.activity_splash);
        textWelcome = (TextView)findViewById(R.id.textWelcome);
       // splashFrame = findViewById(R.id.splashFrame);
        progress = findViewById(R.id.load_progress_splash);

        textWelcome.setText(getString(R.string.splash_progress));
        showLoadProgress(true);
        autoLogin();
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showLoadProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
   //     splashFrame.setVisibility(show ? View.GONE : View.VISIBLE);
     //   splashFrame.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1);
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        progress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }



    private void autoLogin() {
        String username = Functions.getSharedUsername(this);
        String token = Functions.getSharedToken(this);
        if ((!username.equals("")) && (!token.equals(""))) {
            autoLoginTask = new SplashActivity.AutoLoginTask(username, token);
            autoLoginTask.execute((Void) null);
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }



    public class AutoLoginTask extends AsyncTask<Void, Void, UserDTO> {
        private final String mUsername;
        private final String mToken;

        AutoLoginTask(String username, String token) {
            mUsername = username;
            mToken = token;
        }

        @Override
        protected UserDTO doInBackground(Void... params) {
            TokenAndNameDTO tokenAndNameDTO = new TokenAndNameDTO();
            tokenAndNameDTO.setUsername(mUsername);
            tokenAndNameDTO.setToken(mToken);

            Call<UserDTO> call = NetworkService.getInstance().getJSONApi().getUser(tokenAndNameDTO);
            try { return call.execute().body(); } catch (Exception e) { return null; }
        }

        @Override
        protected void onPostExecute(final UserDTO transactionResult) {
            if (transactionResult!=null) {

                textWelcome.setText(getResources().getString(R.string.pin_done_message));
            //    showLoadProgress(false);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                showLoadProgress(false);

                Intent intent = new Intent(SplashActivity.this, PINActivity.class);
                startActivity(intent);
                finish();


               // Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
               // startActivity(intent);
               // finish();
            }
            else
            {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            autoLoginTask = null;
        }
        @Override
        protected void onCancelled() {
            autoLoginTask = null;
        }
    }
}
