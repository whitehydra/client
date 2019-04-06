package com.fadeev.bgtu.client;



import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fadeev.bgtu.client.dto.AuthorizationDTO;
import com.fadeev.bgtu.client.dto.UserDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class HomeActivity extends AppCompatActivity {

    ProfileFragment profileFragment;
    PortfolioFragment portfolioFragment;
    UploadFragment uploadFragment;
    UserLoginTask userLoginTask;


    Toolbar toolbar;
    UserDTO userDTO;


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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolBarListener();





        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }


    public void toolBarListener(){
        Toolbar.OnMenuItemClickListener toolbarListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.update):
                        setTitle("Update");
                        break;
                    case (R.id.options):
                        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(intent);
                      //  setTitle("Options");
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


    public void loginExecute(String username, String password, int postExecuteOption){
        switch (postExecuteOption){
            case 1:
                profileFragment.loadData();
                break;
        }

        userLoginTask = new UserLoginTask(username, password, postExecuteOption);
        userLoginTask.execute((Void) null);
    }


    public class UserLoginTask extends AsyncTask<Void, Void, UserDTO> {

        private final String mUsername;
        private final String mPassword;
        private final int mPostExecuteOption;

        UserLoginTask(String email, String password, int postExecuteOption) {
            mUsername = email;
            mPassword = password;
            mPostExecuteOption = postExecuteOption;

        }

        @Override
        protected UserDTO doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            AuthorizationDTO authorizationDTO = new AuthorizationDTO();
            authorizationDTO.setUsername(mUsername);
            authorizationDTO.setPassword(mPassword);

            try {
                return template.postForObject(Constants.URL.POST_AUTHORIZATION,authorizationDTO,UserDTO.class);

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final UserDTO transactionResult) {
            if (transactionResult!=null) {
                userDTO = transactionResult;


                switch (mPostExecuteOption){
                    case 1:
                        profileFragment.printData();
                        break;
                }


            } else {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() { }
    }

}
