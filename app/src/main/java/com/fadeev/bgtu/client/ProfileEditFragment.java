package com.fadeev.bgtu.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.UserDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileEditFragment extends Fragment {
    String TAG = "Profile edit fragment";

    Button apply;
    Button cancel;
    EditActivity editActivity;
    UserDTO userDTO;
    EditText numberValue;
    EditText mailValue;
    EditText infoValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.edit_profile));
        return inflater.inflate(R.layout.fragment_profile_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "View created");
        editActivity = (EditActivity)getActivity();
        numberValue = editActivity.findViewById(R.id.edNumberValue);
        mailValue = editActivity.findViewById(R.id.edMailValue);
        infoValue = editActivity.findViewById(R.id.edInfoValue);
        createButtons();
        loadData();
    }

    private void createButtons(){
        apply = editActivity.findViewById(R.id.edApplyProfileEdit);
        cancel = editActivity.findViewById(R.id.edCancelProfileEdit);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == apply)updateInfo();
                if(v == cancel)editActivity.finish();
            }
        };
        apply.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
    }


    private void updateInfo(){

        List<Object> postData = new ArrayList<>();
        TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(editActivity),Functions.getSharedToken(editActivity));
        HashMap<String, String> valuesText = new HashMap<>();
        valuesText.put("phone", numberValue.getText().toString());
        valuesText.put("mail", mailValue.getText().toString());
        valuesText.put("info", infoValue.getText().toString());

        postData.add(token);
        postData.add(valuesText);


        Call<Boolean> call = NetworkService.getInstance().getJSONApi().editProfile(postData);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()!=null){
                    if(response.body()){
                        Toast.makeText(editActivity, getResources().getString(R.string.edit_complete_profile), Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(editActivity, getResources().getString(R.string.edit_error_data), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(editActivity, getResources().getString(R.string.edit_error_connection), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void loadData(){
            TokenAndNameDTO tokenAndNameDTO = new TokenAndNameDTO();
            tokenAndNameDTO.setUsername(Functions.getSharedUsername(editActivity));
            tokenAndNameDTO.setToken(Functions.getSharedToken(editActivity));

            Call<UserDTO> call = NetworkService.getInstance().getJSONApi().getUser(tokenAndNameDTO);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if(response.body()!=null){
                        userDTO = response.body();
                        printData();
                    }
                }
                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {

                }
            });
    }


    private void printData(){
        numberValue.setText(userDTO.getPhone());
        mailValue.setText(userDTO.getMail());
        infoValue.setText(userDTO.getInfo());
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"detached");
    }

}
