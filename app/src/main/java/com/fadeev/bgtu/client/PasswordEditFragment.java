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
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PasswordEditFragment extends Fragment {
    String TAG = "Password edit fragment";

    Button apply;
    Button cancel;
    EditActivity editActivity;
    EditText oldPassword;
    EditText newPassword;
    EditText repeatPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.edit_password));
        return inflater.inflate(R.layout.fragment_password_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "View created");
        editActivity = (EditActivity)getActivity();
        oldPassword = editActivity.findViewById(R.id.edOldPasswordValue);
        newPassword = editActivity.findViewById(R.id.edNewPasswordValue);
        repeatPassword = editActivity.findViewById(R.id.edRepeatPasswordValue);
        createButtons();
    }

    private void createButtons(){
        apply = editActivity.findViewById(R.id.edApplyPasswordEdit);
        cancel = editActivity.findViewById(R.id.edCancelPasswordEdit);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == apply)updatePassword();
                if(v == cancel)editActivity.finish();
            }
        };
        apply.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
    }

    private void updatePassword(){

        if(newPassword.getText().toString().equals(repeatPassword.getText().toString())){
            List<Object> postData = new ArrayList<>();
            TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(editActivity),Functions.getSharedToken(editActivity));
            HashMap<String, String> valuesPassword = new HashMap<>();
            try {
                valuesPassword.put("oldPassword", Functions.generateHash(oldPassword.getText().toString()));
                valuesPassword.put("newPassword", Functions.generateHash(newPassword.getText().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            postData.add(token);
            postData.add(valuesPassword);


            Call<Boolean> call = NetworkService.getInstance().getJSONApi().editPassword(postData);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.body()!=null){
                        if(response.body()){
                            oldPassword.setText("");
                            newPassword.setText("");
                            repeatPassword.setText("");
                            Toast.makeText(editActivity, getResources().getString(R.string.edit_complete_password), Toast.LENGTH_LONG).show();
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
        else Toast.makeText(editActivity, getResources().getString(R.string.edit_error_repeat), Toast.LENGTH_LONG).show();
    }



    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"detached");
    }
}
