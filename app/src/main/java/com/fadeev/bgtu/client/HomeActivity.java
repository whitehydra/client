package com.fadeev.bgtu.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.CriterionDTO;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TypeDTO;
import com.fadeev.bgtu.client.dto.UserDTO;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    String TAG = "Home activity";

    ProfileFragment profileFragment;
    PortfolioListFragment portfolioListFragment;
    PortfolioFragment portfolioFragment;
    UploadFragment uploadFragment;
    UsersFragment usersFragment;
    BottomNavigationView navigationView;

    Toolbar toolbar;
    UserDTO userDTO;
    UserDTO viewUserDTO;
    PortfolioDTO portfolioDTO;
    List<UserDTO> usersList;
    FragmentManager fragmentManager;

    Boolean update;
    Boolean userView = false;
    int fragmentID;

    FrameLayout homeFrame;
    View progress;

    List<CategoryDTO> categories;
    List<CriterionDTO> criteria;
    List<TypeDTO> types;


    public void setViewUserDTO(UserDTO viewUserDTO) { this.viewUserDTO = viewUserDTO; }
    public void setUserView(Boolean userView) { this.userView = userView; }

    public ProfileFragment getProfileFragment() { return profileFragment; }
    public PortfolioListFragment getPortfolioListFragment() { return portfolioListFragment; }
    public FragmentManager getHomeFragmentManager() { return fragmentManager; }


    public void disconnect(){
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    if(userView){
                        userView = false;
                        profileFragment.update();
                        fragmentManager.beginTransaction().replace(R.id.homeFrame, profileFragment).commit();
                    }
                    else fragmentManager.beginTransaction().replace(R.id.homeFrame, profileFragment).commit();
                    return true;
                case R.id.navigation_portfolio:
                    if(userView){
                        userView = false;
                        portfolioListFragment.getPortfolioList();
                        fragmentManager.beginTransaction().replace(R.id.homeFrame, portfolioListFragment).commit();
                    }



                    fragmentManager.beginTransaction().replace(R.id.homeFrame, portfolioListFragment).commit();
                    return true;
                case R.id.navigation_upload:
                    fragmentManager.beginTransaction().replace(R.id.homeFrame, uploadFragment).commit();
                    return true;
                case R.id.navigation_users:
                    fragmentManager.beginTransaction().replace(R.id.homeFrame, usersFragment).commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileFragment = new ProfileFragment();
        portfolioListFragment = new PortfolioListFragment();
        uploadFragment = new UploadFragment();
        portfolioFragment = new PortfolioFragment();
        usersFragment = new UsersFragment();
        fragmentManager =  getSupportFragmentManager();
        update = false;

        fragmentManager.beginTransaction().add(R.id.homeFrame, profileFragment).commit();

        setTheme(Functions.getSharedTheme(this));
        Functions.setLocale(this);
        setContentView(R.layout.activity_home);

        progress = findViewById(R.id.load_progress);
        homeFrame = findViewById(R.id.homeFrame);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(Functions.getSharedColor(this)));
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolBarListener();
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    public void toolBarListener(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.update):
                        if(fragmentID == 1) profileFragment.getProfile();
                        if(fragmentID == 2) portfolioListFragment.getPortfolioList();
                        if(fragmentID == 3) uploadFragment.getCategories();
                        if(fragmentID == 4) portfolioFragment.printData();
                        break;
                    case (R.id.options):
                        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showLoadProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        homeFrame.setVisibility(show ? View.GONE : View.VISIBLE);
        homeFrame.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                homeFrame.setVisibility(show ? View.GONE : View.VISIBLE);
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


    private void hideNavigationUsers(){
        Menu navMenu = navigationView.getMenu();
        navMenu.findItem(R.id.navigation_users).setVisible(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
