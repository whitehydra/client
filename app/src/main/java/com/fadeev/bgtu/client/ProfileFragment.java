package com.fadeev.bgtu.client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.UserDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    String TAG = "Profile fragment";

    HomeActivity homeActivity;
    TextView pfNameValue;
    TextView pfPositionValue;
    TextView pfFacultyValue;
    TextView pfGroupValue;
    TextView pfNumberValue;
    TextView pfMailValue;
    TextView pfInfoValue;
    CircleImageView pfAvatar;

    public ProfileFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_profile));
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        Log.d(TAG, "View created");
        pfNameValue = getView().findViewById(R.id.pfNameValue);
        pfPositionValue = getView().findViewById(R.id.pfPositionValue);
        pfFacultyValue = getView().findViewById(R.id.pfFacultyValue);
        pfGroupValue = getView().findViewById(R.id.pfGroupValue);
        pfNumberValue = getView().findViewById(R.id.pfNumberValue);
        pfMailValue = getView().findViewById(R.id.pfMailValue);
        pfInfoValue = getView().findViewById(R.id.pfInfoValue);
        pfAvatar = getView().findViewById(R.id.pfAvatar);

        homeActivity = (HomeActivity)getActivity();

        homeActivity.fragmentID = 1;
        update();
    }

    public void update(){
        if(homeActivity.userDTO!=null){
            printData();
        }
        else {
            homeActivity.showLoadProgress(true);
            getProfile();
        }
    }


    public void getProfile(){
        if(!homeActivity.userView){
            TokenAndNameDTO tokenAndNameDTO = new TokenAndNameDTO();
            tokenAndNameDTO.setUsername(Functions.getSharedUsername(homeActivity));
            tokenAndNameDTO.setToken(Functions.getSharedToken(homeActivity));

            Call<UserDTO> call = NetworkService.getInstance().getJSONApi().getUser(tokenAndNameDTO);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if(response.body()!=null){
                        homeActivity.userDTO = response.body();
                        loadAvatar();
                        printData();
                        homeActivity.showLoadProgress(false);
                    }
                    else homeActivity.disconnect();
                }
                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                }
            });
        }
    }

    public void loadData(){
        pfNameValue.setText(getString(R.string.home_progress));
    }

    public void printData(){
        Log.d(TAG, "Print data");
        UserDTO user;

        if(homeActivity.userView)user = homeActivity.viewUserDTO;
        else user = homeActivity.userDTO;


        pfNameValue.setText(user.getName());
        pfPositionValue.setText(user.getLevel());
        pfFacultyValue.setText(user.getFaculty().getShortName());
        pfGroupValue.setText(user.getGroup().getShortName());
        pfNumberValue.setText(user.getPhone());
        pfMailValue.setText(user.getMail());
        pfInfoValue.setText(user.getInfo());
        if(Functions.checkAvatar(getContext()))drawAvatar();

    }

    public void loadAvatar(){
        Log.d(TAG,"Load avatar");
        final String url = Constants.URL.AVATARS + Functions.getSharedUsername(homeActivity);
        Call<ResponseBody> call = NetworkService.getInstance().getJSONApi().downloadFileWithUrl(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    LoadAvatarTask loadAvatarTask = new LoadAvatarTask(
                            getContext(), response.body(),Constants.FILES.AVATAR);
                    loadAvatarTask.execute();
                    Log.d(TAG, "Загрузка файла...");
                }
                else Log.d(TAG,"Ошибка загрузки");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"Ошибка подключения");
            }
        });
    }

    public void drawAvatar(){
        Drawable image = Drawable.createFromPath(getContext().getExternalFilesDir(null) +
                File.separator + Constants.FILES.AVATAR);

        pfAvatar.setImageDrawable(Functions.resize(getContext(),image,500));
    }


    public class LoadAvatarTask extends AsyncTask<Void, Void, Boolean> {
        Context context;
        ResponseBody responseBody;
        String filename;

        LoadAvatarTask(Context context, ResponseBody responseBody, String filename){
            this.context = context;
            this.responseBody = responseBody;
            this.filename = filename;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return Functions.writeResponseBodyToDisk(context, responseBody,filename);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            Log.d(TAG,"Результат загрузки: " + result);
            drawAvatar();
        }
    }




    @Override
    public void onDetach() {
        if(homeActivity.userView){
            homeActivity.userView = false;
        }
        super.onDetach();
        Log.d(TAG, "View detach");
    }

}
