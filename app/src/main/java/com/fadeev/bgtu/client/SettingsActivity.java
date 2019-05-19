package com.fadeev.bgtu.client;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.file.OpenFileDialog;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends PreferenceActivity {
    static String TAG = "Settings activity";

    LinearLayout root;
    private AlertDialog.Builder ad;
    boolean close = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Functions.setLocale(this);
        setTheme(Functions.getSharedTheme(this));
        super.onCreate(savedInstanceState);
        createDialog();
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainSettingsFragment()).commit();
    }

    public static class MainSettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
            bindSummaryValue(findPreference("language_preference"));
            bindSummaryValue(findPreference("size_list_preference"));

            final ListPreference changeLanguage = (ListPreference)findPreference("language_preference");
            Preference buttonEditProfile = findPreference("edit_profile_preference");
            Preference buttonEditPassword = findPreference("edit_password_preference");
            Preference buttonAbout = findPreference("about_preference");
            Preference buttonExit = findPreference("key_exit_preference");
            Preference buttonAvatar = findPreference("load_avatar_preference");



            changeLanguage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String[] languages = getResources().getStringArray(R.array.language_array_values);
                    for(int i = 0; i < languages.length; i++){
                        if(languages[i].equals(newValue.toString())){
                            changeLanguage.setSummary(getResources().getStringArray(R.array.language_array)[i]);
                        }
                    }
                    Functions.setLocale(getActivity());
                    return true;
                }
            });

            buttonExit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences sPref = preference.getContext().getSharedPreferences(Constants.PREFERENCES.MAIN,MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(Constants.PREFERENCES.USERNAME,"");
                    ed.putString(Constants.PREFERENCES.TOKEN,"");
                    ed.commit();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
            });
            buttonAvatar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    openFile(getActivity());
                    return true;
                }
            });
        }
    }

    public static void openFile(final Context context){
        OpenFileDialog openFileDialog = new OpenFileDialog(context)
                .setFilter(".*\\.jpg")
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(final String fileName) {
                        Toast.makeText(context.getApplicationContext(), fileName, Toast.LENGTH_LONG).show();

                        File originalFile = new File(fileName);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),originalFile);

                        String username = Functions.getSharedUsername(context);
                        String token = Functions.getSharedToken(context);
                        RequestBody usernamePart = RequestBody.create(MultipartBody.FORM, username);
                        RequestBody tokenPart = RequestBody.create(MultipartBody.FORM,token);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file",originalFile.getName(),requestFile);
                        Call<String> call = NetworkService.getInstance().getJSONApi().uploadAvatar(usernamePart,tokenPart, body);

                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = response.body().substring(0,response.body().indexOf(" "));
                                if(result.equals("Done"))
                                {
                                    String resultFilename = response.body().substring(response.body().indexOf(" "));
                                    Log.d(TAG, "Загрузка завершена. Имя файла =" + resultFilename);
                                    Toast.makeText(context.getApplicationContext(), "Загрузка завершена", Toast.LENGTH_LONG).show();
                                }
                                else Log.d(TAG, "Ошибка загрузки файла");
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d(TAG, "Ошибка подключения");
                            }
                        });
                    }
                });
        openFileDialog.show();
    }

    private static void bindSummaryValue(Preference preference){
        preference.setOnPreferenceChangeListener(listener);
        listener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(),""));
    }

    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference)preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >=0
                        ? listPreference.getEntries()[index]
                        : null);
            } else if (preference instanceof EditTextPreference){
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
           // root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent().getParent();
        }
        else {
            root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        }
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_settings,root,false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setBackgroundColor(getResources().getColor(Functions.getSharedColor(this)));
        root.addView(toolbar,0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void createDialog(){
        String title = getResources().getString(R.string.settings_exit_title);
        String message = getResources().getString(R.string.settings_exit_message);
        String okText = getResources().getString(R.string.settings_exit_ok);

        ad = new AlertDialog.Builder(this);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                close = true;
                finish();
            }
        });
    }






    @Override
    public void onBackPressed() {
        ad.show();
    }
}
