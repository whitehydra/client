package com.fadeev.bgtu.client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment profileEdit;
    Fragment passwordEdit;
    Fragment about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Functions.getSharedTheme(this));
        Functions.setLocale(this);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(Functions.getSharedColor(this)));
        setSupportActionBar(toolbar);
        fragmentManager =  getSupportFragmentManager();
        initFragment();
    }

    private void initFragment(){
        profileEdit = new ProfileEditFragment();
        passwordEdit = new PasswordEditFragment();
        about = new AboutFragment();

        Bundle extras = getIntent().getExtras();
        String fragment = extras.getString("fragment");
        if(Objects.equals(fragment, "editProfile"))fragmentManager.beginTransaction().replace(R.id.editFrame, profileEdit).commit();
        if(Objects.equals(fragment, "editPassword"))fragmentManager.beginTransaction().replace(R.id.editFrame, passwordEdit).commit();
        if(Objects.equals(fragment, "about"))fragmentManager.beginTransaction().replace(R.id.editFrame, about).commit();

    }
}

