package com.fadeev.bgtu.client;

import android.content.Context;
import android.content.Intent;
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
    HomeActivity homeActivity;
    TextView pfNameValue;
    TextView pfPositionValue;
    TextView pfFacultyValue;
    TextView pfGroupValue;
    TextView pfNumberValue;
    TextView pfMailValue;
    TextView pfInfoValue;
    CircleImageView pfAvatar;



    final String TAG = "ProfileFragment";

   // private OnFragmentInteractionListener mListener;

    public ProfileFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Fragment onCreate");

//        }
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


      //  pfNameValue.setText(homeActivity.userDTO.getUsername());
    }

    public void update(){
        if(homeActivity.userDTO!=null){
            printData();
        }
        else getProfile();
    }



    public void getProfile(){
        TokenAndNameDTO tokenAndNameDTO = new TokenAndNameDTO();
        tokenAndNameDTO.setUsername(Functions.getSharedUsername(homeActivity));
        tokenAndNameDTO.setToken(Functions.getSharedToken(homeActivity));

        Call<UserDTO> call = NetworkService.getInstance().getJSONApi().postTokenGetUser(tokenAndNameDTO);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if(response.body()!=null){
                    homeActivity.userDTO = response.body();
                    loadAvatar();
                    printData();
                }
                else homeActivity.logout();
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {

            }
        });
    }




    public void loadData(){
        pfNameValue.setText("loading...");
    }

    public void printData(){
        Log.d(TAG, "Print data");
        pfNameValue.setText(homeActivity.userDTO.getName());
        pfPositionValue.setText(homeActivity.userDTO.getLevel());
        pfFacultyValue.setText(homeActivity.userDTO.getFaculty());
        pfGroupValue.setText(homeActivity.userDTO.getStudyGroup());
        pfNumberValue.setText(homeActivity.userDTO.getPhone());
        pfMailValue.setText(homeActivity.userDTO.getMail());
        pfInfoValue.setText(homeActivity.userDTO.getInfo());
        if(Functions.checkAvatar(getContext()))drawAvatar();

   //     Log.d(TAG,"avatar = " + homeActivity.userDTO.getAvatar());
    }

    public void loadAvatar(){
        Log.d(TAG,"Load avatar");
        final String url = Constants.URL.AVATARS + Functions.getSharedUsername(homeActivity);
        Log.d(TAG, "type = " + Functions.getType(url));
        Call<ResponseBody> call = NetworkService.getInstance().getJSONApi().downloadFileWithUrl(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    LoadAvatarTask loadAvatarTask = new LoadAvatarTask(
                            getContext(), response.body(),Constants.FILES.AVATAR);
                    loadAvatarTask.execute();
                    Log.d(TAG, "getting file...");

                }
                else Log.d(TAG,"getting file error");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"Error");
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
            Log.d(TAG,"download result: " + result);
            drawAvatar();
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Profile detach");
    }

}
