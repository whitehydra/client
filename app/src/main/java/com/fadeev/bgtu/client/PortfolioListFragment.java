package com.fadeev.bgtu.client;

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

import java.util.ArrayList;
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
                    getCategories(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<PortfolioDTO>> call, Throwable t) { }
        });
    }


    public void createAdapter(){
        adapter = new PortfolioAdapter(homeActivity,R.layout.portfolio_list_layout,list);
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
                homeActivity.fragmentManager.beginTransaction().replace(R.id.homeFrame, homeActivity.portfolioFragment).commit();
            }
        });

    }

    public void printCategories(final PortfolioAdapter portfolioAdapter){
        categories.add(0,new CategoryDTO(-1,"Все категории","0"));
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
}
