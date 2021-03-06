package com.fadeev.bgtu.client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.fadeev.bgtu.client.adapters.PortfolioAdapter;
import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioListFragment extends Fragment {
    String TAG = "Portfolio list fragment";

    HomeActivity homeActivity;
    ListView portfolioList;
    EditText searchBar;
    Spinner categorySpinner;
    List<PortfolioDTO> list;
    List<CategoryDTO> categories;
    PortfolioAdapter adapter;

    CharSequence search = null;
    CharSequence choose = null;

    @SuppressLint("SimpleDateFormat") final SimpleDateFormat date = new SimpleDateFormat("dd MMM yyyy");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_portfolio));
        return inflater.inflate(R.layout.fragment_portfolio_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        homeActivity = (HomeActivity)getActivity();
        portfolioList = view.findViewById(R.id.poPortfolioList);
        searchBar = view.findViewById(R.id.pfSearchBar);
        categorySpinner = view.findViewById(R.id.pfCategorySpinner);
        homeActivity.fragmentID = 2;
        homeActivity.showLoadProgress(true);

        portfolioList.setNestedScrollingEnabled(true);
        getPortfolioList();
    }

    public void getPortfolioList(){
        if(!homeActivity.userView){
            List<Object> postData = new ArrayList<>();
            TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));
            postData.add(token);

            Call<List<PortfolioDTO>> call = NetworkService.getInstance().getJSONApi().getPortfolioList(postData);
            call.enqueue(new Callback<List<PortfolioDTO>>() {
                @Override
                public void onResponse(Call<List<PortfolioDTO>> call, Response<List<PortfolioDTO>> response) {
                    if(response.body()!=null){
                        Log.d(TAG, "Портфолио получено");
                        list = response.body();
                        createAdapter();
                        createListener();
              //          homeActivity.showLoadProgress(false);
                        getCategories(adapter);
                    }
                }
                @Override
                public void onFailure(Call<List<PortfolioDTO>> call, Throwable t) {
                    homeActivity.printError();
                }
            });
        }
        else {
            List<Object> postData = new ArrayList<>();
            TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));

            HashMap<String, String> userInfo = new HashMap<>();
            userInfo.put("name",homeActivity.viewUserDTO.getName());
            userInfo.put("faculty",homeActivity.viewUserDTO.getFaculty().getShortName());
            userInfo.put("group",homeActivity.viewUserDTO.getGroup().getShortName());

            postData.add(token);
            postData.add(userInfo);


            postData.add(token);

            Call<List<PortfolioDTO>> call = NetworkService.getInstance().getJSONApi().getUserPortfolioList(postData);
            call.enqueue(new Callback<List<PortfolioDTO>>() {
                @Override
                public void onResponse(Call<List<PortfolioDTO>> call, Response<List<PortfolioDTO>> response) {
                    if(response.body()!=null){
                        Log.d(TAG, "Портфолио получено");
                        list = response.body();
                        createAdapter();
                        createListener();
                        getCategories(adapter);
                    }
                }
                @Override
                public void onFailure(Call<List<PortfolioDTO>> call, Throwable t) { }
            });
        }
    }


    private class SortPortfolio implements Comparator<PortfolioDTO>{
        public int compare(PortfolioDTO a, PortfolioDTO b){
            try {
                Date d1 = date.parse(a.getDate_event());
                Date d2 = date.parse(b.getDate_event());
                return d2.compareTo(d1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    public void createAdapter(){
        Collections.sort(list, new SortPortfolio());
        adapter = new PortfolioAdapter(homeActivity,R.layout.portfolio_list_item,list);
        portfolioList.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search = s;
                adapter.getSearchFilter(choose).filter(search);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public void createListener(){
        portfolioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                homeActivity.portfolioDTO = (PortfolioDTO)portfolioList.getItemAtPosition(position);
                homeActivity.fragmentManager.beginTransaction().replace(R.id.homeFrame, homeActivity.portfolioFragment).addToBackStack(null).commit();
            }
        });

    }

    public void printCategories(final PortfolioAdapter portfolioAdapter){
        Log.d(TAG, "Категорий получено: " + categories.size());

        ArrayAdapter<CategoryDTO> adapter = new ArrayAdapter<CategoryDTO>(
                homeActivity,android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose = categories.get(position).getName_category();
                portfolioAdapter.getSearchFilter(choose).filter(search);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getCategories(final PortfolioAdapter portfolioAdapter){
        Call<List<CategoryDTO>> call = NetworkService.getInstance().getJSONApi().getCategories();
        call.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if(response.body()!=null){
                    categories = response.body();
                    categories.add(0,new CategoryDTO(-1,homeActivity.getResources().getString(R.string.portfolio_list_all_categories)));
                    printCategories(portfolioAdapter);
                    homeActivity.showLoadProgress(false);
                }
            }
            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
                Log.d(TAG, "Ошибка получения категорий");
            }
        });
    }

    @Override
    public void onDetach() {
        if(homeActivity.userView){
            homeActivity.userView = false;
        }
        super.onDetach();
    }
}
