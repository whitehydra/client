package com.fadeev.bgtu.client.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fadeev.bgtu.client.Functions;
import com.fadeev.bgtu.client.HomeActivity;
import com.fadeev.bgtu.client.R;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioAdapter extends ArrayAdapter<PortfolioDTO> implements Filterable {
    private String TAG = "Portfolio adapter";

    private HomeActivity homeActivity;

    private List<PortfolioDTO> originalData;
    private List<PortfolioDTO> filteredData;
    private AlertDialog.Builder ad;
    private PortfolioDTO portfolio;

    public int getCount(){
        return filteredData.size();
    }
    public PortfolioDTO getItem(int position){
        return filteredData.get(position);
    }

    public PortfolioAdapter(HomeActivity homeActivity, int resource, List<PortfolioDTO> portfolio) {
        super(homeActivity, resource, portfolio);
        this.homeActivity = homeActivity;
        originalData = portfolio;
        filteredData = portfolio;
        createDialog();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PortfolioDTO currentItem = filteredData.get(position);

        String name = currentItem.getName();
        String date = currentItem.getDate_event();
        String category = currentItem.getCategory().getName_category();
        String type = currentItem.getType().getName_type();

        LayoutInflater inflater = LayoutInflater.from(homeActivity);
        View rowView = inflater.inflate(R.layout.portfolio_list_item, parent, false);

        TextView tvName = rowView.findViewById(R.id.ltName);
        TextView tvDate = rowView.findViewById(R.id.ltDate);
        TextView tvCategory = rowView.findViewById(R.id.ltCategory);
        TextView tvType = rowView.findViewById(R.id.ltType);
        ImageButton tvClose = rowView.findViewById(R.id.ltClose);
        if(homeActivity.getUserView())tvClose.setVisibility(View.INVISIBLE);

        tvName.setText(name);
        tvDate.setText(date);
        tvCategory.setText(category);
        tvType.setText(type);

    //    createDialog();

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portfolio = currentItem;
                ad.show();
            }
        });
        return rowView;
    }

    public void createDialog(){
        String title = homeActivity.getResources().getString(R.string.list_element_title);
        String message = homeActivity.getResources().getString(R.string.list_element_message);
        String okText = homeActivity.getResources().getString(R.string.list_element_ok);
        String cancelText = homeActivity.getResources().getString(R.string.list_element_cancel);

      //  final PortfolioDTO currentItem = portfolio;
        ad = new AlertDialog.Builder(homeActivity);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Object> postData = new ArrayList<>();
                TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));
                HashMap<String, Integer> portfolioID = new HashMap<>();

                final PortfolioDTO currentItem = portfolio;
                portfolioID.put("id_portfolio",currentItem.getId_portfolio());
                postData.add(token);
                postData.add(portfolioID);

                Call<Integer> call = NetworkService.getInstance().getJSONApi().deletePortfolio(postData);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.body()!=null){
                            Toast.makeText(getContext().getApplicationContext(), homeActivity.getResources().getString(R.string.portfolio_note) + " " +
                                    currentItem.getName() + " " + homeActivity.getResources().getString(R.string.portfolio_deleted), Toast.LENGTH_LONG).show();
                            filteredData.remove(currentItem);
                            notifyDataSetChanged();
                        } else Toast.makeText(getContext().getApplicationContext(), homeActivity.getResources().getString(R.string.portfolio_delete_error), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                    }
                });
            }
        });
        ad.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    @NonNull
    public Filter getSearchFilter(final CharSequence choose) {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    results.values = originalData;
                    results.count = originalData.size();
                }
                else{
                    List<PortfolioDTO> filterResultsData = new ArrayList<>();

                    for(PortfolioDTO data : originalData){
                        if(data.getName().contains(constraint.toString())){
                            filterResultsData.add(data);
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (List<PortfolioDTO>)results.values;
                notifyDataSetChanged();
                getCategoryFilter().filter(choose);
            }
        };
    }

    @NonNull
    public Filter getCategoryFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    results.values = filteredData;
                    results.count = filteredData.size();
                }
                else{
                    List<PortfolioDTO> filterResultsData = new ArrayList<>();

                    for(PortfolioDTO data : filteredData){
                        if(data.getCategory().getName_category().equals(constraint.toString()) ||
                        constraint.toString().equals(homeActivity.getResources().getString(R.string.portfolio_list_all_categories))){
                            filterResultsData.add(data);
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (List<PortfolioDTO>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
