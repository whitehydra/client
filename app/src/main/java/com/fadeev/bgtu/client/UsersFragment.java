package com.fadeev.bgtu.client;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.fadeev.bgtu.client.adapters.UsersAdapter;
import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.FacultyDTO;
import com.fadeev.bgtu.client.dto.GroupDTO;
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
    List<FacultyDTO> faculties;
    List<GroupDTO> groups;

    CharSequence currentFaculty = null;
    CharSequence currentGroup = null;

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
                    getFaculties(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) { }
        });
    }


    public void drawFaculties(final UsersAdapter usersAdapter){
        faculties.add(0,new FacultyDTO(-1,getResources().getString(R.string.users_list_all_faculties),
                getResources().getString(R.string.users_list_all_faculties)));
        Log.d(TAG, "Факультетов получено: " + faculties.size());

        ArrayAdapter<FacultyDTO> adapter = new ArrayAdapter<FacultyDTO>(
                homeActivity,android.R.layout.simple_spinner_item, faculties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facultySpinner.setAdapter(adapter);
    }


    public void drawGroups(final UsersAdapter usersAdapter){
        groups.add(0,new GroupDTO(-1,getResources().getString(R.string.users_list_all_groups),
                getResources().getString(R.string.users_list_all_groups)));
        ArrayAdapter<GroupDTO> adapter = new ArrayAdapter<GroupDTO>(
                homeActivity,android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(adapter);

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentGroup = groups.get(position).getShortName();
                usersAdapter.getFacultyFilter(currentGroup).filter(currentFaculty);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    public void getFaculties(final UsersAdapter usersAdapter){
        Call<List<FacultyDTO>> call = NetworkService.getInstance().getJSONApi().getFaculties();
        call.enqueue(new Callback<List<FacultyDTO>>() {
            @Override
            public void onResponse(Call<List<FacultyDTO>> call, Response<List<FacultyDTO>> response) {
                if(response.body()!=null){
                    faculties = response.body();
                    drawFaculties(usersAdapter);
             //       homeActivity.showLoadProgress(false);
                }
            }
            @Override
            public void onFailure(Call<List<FacultyDTO>> call, Throwable t) {
                Log.d(TAG, "Ошибка получения факультетов");
            }
        });
    }



    public void getGroups(final UsersAdapter usersAdapter, int facultyId){
        Call<List<GroupDTO>> call = NetworkService.getInstance().getJSONApi().getGroups(facultyId);
        call.enqueue(new Callback<List<GroupDTO>>() {
            @Override
            public void onResponse(Call<List<GroupDTO>> call, Response<List<GroupDTO>> response) {
                if(response.body()!=null){
                    groups = response.body();
                    Log.d(TAG, "Групп получено: " + groups.size());
                    drawGroups(usersAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<GroupDTO>> call, Throwable t) {
                Log.d(TAG, "Ошибка получения групп");
            }
        });
    }






    public void createAdapter(){
        adapter = new UsersAdapter(homeActivity,R.layout.users_list_item,homeActivity.usersList);
        usersList.setAdapter(adapter);



        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFaculty = faculties.get(position).getShortName();
                adapter.getFacultyFilter(currentGroup).filter(currentFaculty);
                getGroups(adapter,faculties.get(position).getFacultyID());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "View detach");
    }

}
