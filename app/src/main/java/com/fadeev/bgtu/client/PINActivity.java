package com.fadeev.bgtu.client;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PINActivity extends AppCompatActivity {

    String TAG = "PIN Activity";
    boolean createPIN = false;
    AlertDialog.Builder errorDialog;

    Toolbar toolbar;
    EditText PIN;
    TextView textMessage;
    Button key0;
    Button key1;
    Button key2;
    Button key3;
    Button key4;
    Button key5;
    Button key6;
    Button key7;
    Button key8;
    Button key9;
    Button keyBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Functions.getSharedTheme(this));
        setContentView(R.layout.activity_pin);



        toolbar = (Toolbar) findViewById(R.id.toolbarPIN);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.pin_toolbar_menu);
        toolBarListener();
        setTitle("");
        PIN = findViewById(R.id.pin_text);
        textMessage = findViewById(R.id.text_message);
        textMessage.setVisibility(View.INVISIBLE);
        initKeys();
        initErrorDialog();
        getPinStatus();
    }


    public void toolBarListener(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.pin_options):
                        break;

                    case R.id.exit:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.pin_exit), Toast.LENGTH_LONG).show();
                        SharedPreferences sPref = getSharedPreferences(Constants.PREFERENCES.MAIN,MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString(Constants.PREFERENCES.USERNAME,"");
                        ed.putString(Constants.PREFERENCES.TOKEN,"");
                        ed.commit();

                        Intent intent = new Intent(PINActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.restore:
                        Call<String> call = NetworkService.getInstance().getJSONApi().pinRecovery(Functions.getSharedUsername(getBaseContext()));
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.body()!=null){
                                    Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });


                        break;
                }

                return false;
            }
        });
    }


    public void getPinStatus(){
        Call<Boolean> call = NetworkService.getInstance().getJSONApi().getPinStatus(Functions.getSharedUsername(this));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()!=null){
                    createPIN = response.body();
                    if(createPIN)textMessage.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d(TAG, "Ошибка подключения");
            }
        });
    }



    public void sendPin(){
        List<Object> postData = new ArrayList<>();
        TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(this),Functions.getSharedToken(this));
        HashMap<String, String> pinText = new HashMap<>();
        pinText.put("pin", PIN.getText().toString());

        postData.add(token);
        postData.add(pinText);

        if(createPIN){
            Call<Boolean> call = NetworkService.getInstance().getJSONApi().setPin(postData);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.body()!=null){
                        if(response.body()){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pin_done_message), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PINActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) { }
            });
        }
        else {
            Call<Boolean> call = NetworkService.getInstance().getJSONApi().checkPin(postData);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.body()!=null){
                        if(response.body()){
                            Intent intent = new Intent(PINActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            errorDialog.show();
                            PIN.setText("");
                        }
                    }

                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) { }
            });
        }
    }


    public void initErrorDialog(){
        String title = getResources().getString(R.string.pin_error_title);
        String message = getResources().getString(R.string.pin_error_message);
        String okText = getResources().getString(R.string.pin_error_ok);
        errorDialog = new AlertDialog.Builder(this);
        errorDialog.setTitle(title);
        errorDialog.setMessage(message);
        errorDialog.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }



    public void addCharacter(String input){
        String textPIN = PIN.getText().toString();
        if(textPIN.length() < 4){
            String newPIN = textPIN + input;
            PIN.setText(newPIN);
            if(PIN.getText().toString().length() == 4)sendPin();
        }
    }

    public void deleteCharacter(){
        String textPIN = PIN.getText().toString();
        if(textPIN.length()!=0){
            PIN.setText(textPIN.substring(0,PIN.getText().toString().length()-1));
        }
    }


    public void initKeys(){
        key0 = findViewById(R.id.key_0);
        key1 = findViewById(R.id.key_1);
        key2 = findViewById(R.id.key_2);
        key3 = findViewById(R.id.key_3);
        key4 = findViewById(R.id.key_4);
        key5 = findViewById(R.id.key_5);
        key6 = findViewById(R.id.key_6);
        key7 = findViewById(R.id.key_7);
        key8 = findViewById(R.id.key_8);
        key9 = findViewById(R.id.key_9);
        keyBack = findViewById(R.id.key_back);
        initListener();
    }


    public void initListener(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.key_0:
                        addCharacter("0");
                        break;
                    case R.id.key_1:
                        addCharacter("1");
                        break;
                    case R.id.key_2:
                        addCharacter("2");
                        break;
                    case R.id.key_3:
                        addCharacter("3");
                        break;
                    case R.id.key_4:
                        addCharacter("4");
                        break;
                    case R.id.key_5:
                        addCharacter("5");
                        break;
                    case R.id.key_6:
                        addCharacter("6");
                        break;
                    case R.id.key_7:
                        addCharacter("7");
                        break;
                    case R.id.key_8:
                        addCharacter("8");
                        break;
                    case R.id.key_9:
                        addCharacter("9");
                        break;
                    case R.id.key_back:
                        deleteCharacter();
                        break;
                        default:break;
                }
            }
        };
        key0.setOnClickListener(onClickListener);
        key1.setOnClickListener(onClickListener);
        key2.setOnClickListener(onClickListener);
        key3.setOnClickListener(onClickListener);
        key4.setOnClickListener(onClickListener);
        key5.setOnClickListener(onClickListener);
        key6.setOnClickListener(onClickListener);
        key7.setOnClickListener(onClickListener);
        key8.setOnClickListener(onClickListener);
        key9.setOnClickListener(onClickListener);
        keyBack.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pin_toolbar_menu, menu);
        return true;
    }
}
