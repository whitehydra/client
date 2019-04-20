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
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.CriterionDTO;
import com.fadeev.bgtu.client.dto.FileDTO;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.TypeDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadFragment extends Fragment {
    HomeActivity homeActivity;
    List<CategoryDTO> categories;
    List<CriterionDTO> criteria;
    List<TypeDTO> types;

    Spinner categorySpinner;
    Spinner criterionSpinner;
    Spinner typeSpinner;

    String TAG = "Upload fragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.title_upload));


        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        criterionSpinner = view.findViewById(R.id.criterionSpinner);
        typeSpinner = view.findViewById(R.id.typeSpinner);


        getCategories();
    }



    private void displayCategory(CategoryDTO categoryDTO){
        String name = categoryDTO.getName_category();
        getCriteria(categoryDTO.getCategoryID());
        getTypes(categoryDTO.getCategoryID());
        Toast.makeText(homeActivity,name,Toast.LENGTH_SHORT).show();
    }
    private void displayCriterion(CriterionDTO criterionDTO){
        String name = criterionDTO.getName_criterion();
        Toast.makeText(homeActivity,name,Toast.LENGTH_SHORT).show();
    }
    private void displayType(TypeDTO typeDTO){
        String name = typeDTO.getName_type();
        Toast.makeText(homeActivity,name,Toast.LENGTH_SHORT).show();
    }







    public void addPortfolio(){
        List<Object> postData = new ArrayList<>();
        TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));
        PortfolioDTO portfolio = new PortfolioDTO("name", "eve",
                "pub", 1, 1, 1);

        postData.add(token);
        postData.add(portfolio);

        Call<Integer> call = NetworkService.getInstance().getJSONApi().addPortfolio(postData);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body()!=null)
                    Log.d(TAG, "portfolio num = " + response.body());
                    addFile(response.body());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }


    public void getCategories(){
        Call<List<CategoryDTO>> call = NetworkService.getInstance().getJSONApi().getCategories();
        call.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if(response.body()!=null){
                    categories = response.body();
                    Log.d(TAG, "Категорий получено: " + categories.size());

                    ArrayAdapter<CategoryDTO> adapter = new ArrayAdapter<CategoryDTO>(
                            homeActivity,android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            CategoryDTO category = (CategoryDTO)parent.getSelectedItem();
                            displayCategory(category);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
                Log.d(TAG, "Ошибка получения категорий");
            }
        });
    }


    public void getCriteria(int categoryId){
        Call<List<CriterionDTO>> call = NetworkService.getInstance().getJSONApi().getCriteria(categoryId);
        call.enqueue(new Callback<List<CriterionDTO>>() {
            @Override
            public void onResponse(Call<List<CriterionDTO>> call, Response<List<CriterionDTO>> response) {
                if(response.body()!=null){
                    criteria = response.body();
                    Log.d(TAG, "Критериев получено: " + criteria.size());
                    ArrayAdapter<CriterionDTO> adapter = new ArrayAdapter<CriterionDTO>(
                            homeActivity,android.R.layout.simple_spinner_item, criteria);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    criterionSpinner.setAdapter(adapter);
                    criterionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            CriterionDTO criterion = (CriterionDTO) parent.getSelectedItem();
                            displayCriterion(criterion);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }
            }
            @Override
            public void onFailure(Call<List<CriterionDTO>> call, Throwable t) {
                Log.d(TAG, "Ошибка получения критериев");
            }
        });
    }

    public void getTypes(int categoryId){
        Call<List<TypeDTO>> call = NetworkService.getInstance().getJSONApi().getTypes(categoryId);
        call.enqueue(new Callback<List<TypeDTO>>() {
            @Override
            public void onResponse(Call<List<TypeDTO>> call, Response<List<TypeDTO>> response) {
                if(response.body()!=null){
                    types = response.body();
                    Log.d(TAG, "Типов участия получено:" + types.size());

                    ArrayAdapter<TypeDTO> adapter = new ArrayAdapter<TypeDTO>(
                            homeActivity,android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(adapter);
                    typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TypeDTO type = (TypeDTO)parent.getSelectedItem();
                            displayType(type);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<TypeDTO>> call, Throwable t) {
                Log.d(TAG, "Ошибка получения типов участия");
            }
        });
    }



    public void addFile(int id){

        List<Object> postData = new ArrayList<>();
        TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));
        FileDTO file = new FileDTO("name","src", "type", id);

        postData.add(token);
        postData.add(file);

        Call<String> call = NetworkService.getInstance().getJSONApi().addFile(postData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String answer = response.body();
                Log.d(TAG, "Response - " + answer );
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

}
