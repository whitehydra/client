package com.fadeev.bgtu.client;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.fadeev.bgtu.client.adapters.UsersAdapter;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.UserDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends Fragment {
    String TAG = "Users fragment";
    HomeActivity homeActivity;
    UsersAdapter adapter;
    ListView usersList;
    Spinner facultySpinner;
    Spinner groupSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_users));
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "View created");
        homeActivity = (HomeActivity)getActivity();
        homeActivity.fragmentID = 5;
        usersList = view.findViewById(R.id.usUsersList);
        facultySpinner = view.findViewById(R.id.usFacultySpinner);
        groupSpinner = view.findViewById(R.id.usGroupSpinner);
        usersList.setNestedScrollingEnabled(true);
        getUsersList();
    }

    private void getUsersList(){
        List<Object> postData = new ArrayList<>();
        TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));
        postData.add(token);

        Call<List<UserDTO>> call = NetworkService.getInstance().getJSONApi().getUsersList(postData);
        call.enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if(response.body()!=null){
                    Log.d(TAG, "Список пользователей получен");
                    homeActivity.usersList = response.body();
                    createAdapter();
     //               createListener();
     //               getCategories(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) { }
        });
    }


    public void createAdapter(){
        adapter = new UsersAdapter(homeActivity,R.layout.users_list_item,homeActivity.usersList);
        usersList.setAdapter(adapter);
    }

    

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "View detach");
    }

}
