package com.fadeev.bgtu.client.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fadeev.bgtu.client.HomeActivity;
import com.fadeev.bgtu.client.R;
import com.fadeev.bgtu.client.dto.UserDTO;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<UserDTO> implements Filterable {
    private String TAG = "Users adapter";

    private HomeActivity homeActivity;
    private List<UserDTO> originalData;
    private List<UserDTO> filteredData;


    public UsersAdapter(HomeActivity homeActivity, int resource, List<UserDTO> users){
        super(homeActivity,resource,users);
        this.homeActivity = homeActivity;
        originalData = users;
        filteredData = users;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final UserDTO currentItem = filteredData.get(position);

        String name = currentItem.getName();
        String faculty = currentItem.getFaculty();
        String group = currentItem.getStudyGroup();

        LayoutInflater inflater = LayoutInflater.from(homeActivity);
        View rowView = inflater.inflate(R.layout.users_list_item, parent, false);

        TextView ultName = rowView.findViewById(R.id.ultName);
        TextView ultFaculty = rowView.findViewById(R.id.ultFaculty);
        TextView ultGroup = rowView.findViewById(R.id.ultGroup);
        final ImageButton openProfile = rowView.findViewById(R.id.ultOpenProfile);
        final ImageButton openPortfolio = rowView.findViewById(R.id.ultOpenPortfolio);

        ultName.setText(name);
        ultFaculty.setText(faculty);
        ultGroup.setText(group);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == openProfile){
                    homeActivity.setUserView(true);
                    homeActivity.setViewUserDTO(currentItem);
                    homeActivity.getHomeFragmentManager().beginTransaction().replace(R.id.homeFrame,homeActivity.getProfileFragment()).addToBackStack( "users_list" ).commit();

                }
                if(v == openPortfolio){
                    homeActivity.setUserView(true);
                    homeActivity.setViewUserDTO(currentItem);
                    homeActivity.getHomeFragmentManager().beginTransaction().replace(R.id.homeFrame,homeActivity.getPortfolioListFragment()).addToBackStack( "users_list" ).commit();
                }
            }
        };
        openProfile.setOnClickListener(onClickListener);
        openPortfolio.setOnClickListener(onClickListener);
        return rowView;
    }



}
