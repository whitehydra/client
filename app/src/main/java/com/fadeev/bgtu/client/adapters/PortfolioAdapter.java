package com.fadeev.bgtu.client.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fadeev.bgtu.client.R;
import com.fadeev.bgtu.client.dto.PortfolioDTO;

import java.util.ArrayList;
import java.util.List;

public class PortfolioAdapter extends ArrayAdapter<PortfolioDTO> implements Filterable {
    private String TAG = "Portfolio adapter";
    private Context context;
    private int resource;

    private List<PortfolioDTO> originalData;
    private List<PortfolioDTO> filteredData;


    public int getCount(){
        return filteredData.size();
    }

    public PortfolioDTO getItem(int position){
        return filteredData.get(position);
    }




    public PortfolioAdapter(Context context, int resource, List<PortfolioDTO> portfolio) {
        super(context, resource, portfolio);
        this.context = context;
        this.resource = resource;

        originalData = portfolio;
        filteredData = portfolio;
    }





    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PortfolioDTO currentItem = filteredData.get(position);

        String name = currentItem.getName();
        String date = currentItem.getDate_event();
        String category = currentItem.getCategory().getName_category();
        String type = currentItem.getType().getName_type();

     //   PortfolioDTO portfolio = new PortfolioDTO(name,date,getItem(position).getCategory(),getItem(position).getType());

        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.portfolio_list_layout, parent, false);

        TextView tvName = rowView.findViewById(R.id.ltName);
        TextView tvDate = rowView.findViewById(R.id.ltDate);
        TextView tvCategory = rowView.findViewById(R.id.ltCategory);
        TextView tvType = rowView.findViewById(R.id.ltType);

        tvName.setText(name);
        tvDate.setText(date);
        tvCategory.setText(category);
        tvType.setText(type);



        return rowView;
    }

    @NonNull
    public Filter getSearchFilter() {
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
                    results.values = originalData;
                    results.count = originalData.size();
                }
                else{
                    List<PortfolioDTO> filterResultsData = new ArrayList<>();

                    for(PortfolioDTO data : originalData){
                        if(data.getCategory().getName_category().equals(constraint.toString()) ||
                        constraint.toString().equals("Все категории")){
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
