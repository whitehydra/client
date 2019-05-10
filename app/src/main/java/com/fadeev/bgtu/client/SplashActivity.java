package com.fadeev.bgtu.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.UserDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import retrofit2.Call;

public class SplashActivity extends AppCompatActivity {
    String TAG = "Splash activity";

    private AutoLoginTask autoLoginTask = null;
    private TextView textWelcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textWelcome = (TextView)findViewById(R.id.textWelcome);
        textWelcome.setText(getString(R.string.splash_progress));
        autoLogin();
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

            Call<UserDTO> call = NetworkService.getInstance().getJSONApi().postTokenGetUser(tokenAndNameDTO);
            try { return call.execute().body(); } catch (Exception e) { return null; }
        }

        @Override
        protected void onPostExecute(final UserDTO transactionResult) {
            if (transactionResult!=null) {

                textWelcome.setText("Успех!");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


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
