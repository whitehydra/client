package com.fadeev.bgtu.client;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.CriterionDTO;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TypeDTO;
import com.fadeev.bgtu.client.dto.UserDTO;


import java.util.List;


public class HomeActivity extends AppCompatActivity {

    ProfileFragment profileFragment;
    PortfolioListFragment portfolioListFragment;
    PortfolioFragment portfolioFragment;

    UploadFragment uploadFragment;

    Toolbar toolbar;
    UserDTO userDTO;
    PortfolioDTO portfolioDTO;

    int fragmentID;
    FragmentManager fragmentManager;
    Boolean update;


    List<CategoryDTO> categories;
    List<CriterionDTO> criteria;
    List<TypeDTO> types;



    public void logout(){
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
                    fragmentManager.beginTransaction().replace(R.id.homeFrame, profileFragment).commit();
                    return true;
                case R.id.navigation_portfolio:
                    fragmentManager.beginTransaction().replace(R.id.homeFrame, portfolioListFragment).commit();
                    return true;
                case R.id.navigation_upload:
                    fragmentManager.beginTransaction().replace(R.id.homeFrame, uploadFragment).commit();
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
        fragmentManager =  getSupportFragmentManager();
        update = false;

        fragmentManager.beginTransaction().add(R.id.homeFrame, profileFragment).commit();
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolBarListener();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    public void toolBarListener(){
        Toolbar.OnMenuItemClickListener toolbarListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.update):
                        if(fragmentID == 1) profileFragment.getProfile();
                        if(fragmentID == 2) portfolioListFragment.getPortfolio();
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
        };
        toolbar.setOnMenuItemClickListener(toolbarListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
