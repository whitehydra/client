package com.fadeev.bgtu.client;



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    ProfileFragment profileFragment;
    PortfolioFragment portfolioFragment;
    UploadFragment uploadFragment;


    FragmentManager fragmentManager;





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragmentManager.beginTransaction().replace(R.id.homeFrame, profileFragment).commit();


                    return true;
                case R.id.navigation_dashboard:

                    fragmentManager.beginTransaction().replace(R.id.homeFrame, portfolioFragment).commit();
                    return true;
                case R.id.navigation_notifications:
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
        portfolioFragment = new PortfolioFragment();
        uploadFragment = new UploadFragment();
        fragmentManager =  getSupportFragmentManager();


        fragmentManager.beginTransaction().add(R.id.homeFrame, profileFragment).commit();


        setContentView(R.layout.activity_home);




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

}
