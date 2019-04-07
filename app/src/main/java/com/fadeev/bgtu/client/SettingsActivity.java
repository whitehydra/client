package com.fadeev.bgtu.client;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class SettingsActivity extends PreferenceActivity {

    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainSettingsFragment()).commit();
    }

    private void exit(){

    }


    public static class MainSettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
            bindSummaryValue(findPreference("key_edit_text"));
            bindSummaryValue(findPreference("key_edit_text2"));
            bindSummaryValue(findPreference("key_list_preference"));

            Preference button = findPreference("key_exit");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
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
        }
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
            root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent().getParent();
        }
        else {
            root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        }
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_settings,root,false);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        root.addView(toolbar,0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
