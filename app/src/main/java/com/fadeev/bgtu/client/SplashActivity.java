package com.fadeev.bgtu.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.UserDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SplashActivity extends AppCompatActivity {

    private AutoLoginTask autoLoginTask = null;
    private TextView textWelcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textWelcome = (TextView)findViewById(R.id.textWelcome);
        textWelcome.setText("Загрузка...");
        autoLogin();
    }


    private void autoLogin() {
        SharedPreferences sPref;
        sPref = getSharedPreferences(Constants.PREFERENCES.MAIN, MODE_PRIVATE);
        String username = sPref.getString(Constants.PREFERENCES.USERNAME, "");
        String token = sPref.getString(Constants.PREFERENCES.TOKEN, "");
        if ((!username.equals("")) && (!token.equals(""))) {
            autoLoginTask = new SplashActivity.AutoLoginTask(username, token,this);
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
        private final Context mContext;

        AutoLoginTask(String username, String token, Context context) {
            mUsername = username;
            mToken = token;
            mContext = context;
        }

        @Override
        protected UserDTO doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            TokenAndNameDTO tokenAndNameDTO = new TokenAndNameDTO();
            tokenAndNameDTO.setUsername(mUsername);
            tokenAndNameDTO.setToken(mToken);
            try {
                return template.postForObject(Constants.URL.POST_AUTHENTICATION,tokenAndNameDTO, UserDTO.class);
            } catch (Exception e) {
                return null;
            }
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
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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
