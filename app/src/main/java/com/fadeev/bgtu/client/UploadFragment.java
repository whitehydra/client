package com.fadeev.bgtu.client;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.CriterionDTO;
import com.fadeev.bgtu.client.dto.FileDTO;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.TypeDTO;
import com.fadeev.bgtu.client.file.OpenFileDialog;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadFragment extends Fragment {
    HomeActivity homeActivity;


    Spinner categorySpinner;
    Spinner typeSpinner;

    RadioGroup radioButtons;


    LinearLayout dateEventBlock;
    LinearLayout datePublicationBlock;
    LinearLayout fileLoadBlock1;
    LinearLayout fileLoadBlock2;

    Button fileLoadButton1;
    Button fileLoadButton2;
    Button openCloseButton;
    Button uploadButton;


    TextView dateEventPole;
    TextView datePublicationPole;

    EditText fileLoadPole1;
    EditText fileLoadPole2;
    EditText nameText;



    Calendar selectedTimeEvent = Calendar.getInstance();
    Calendar selectedTimePublication = Calendar.getInstance();

    Boolean additionalFileBlock = false;
    List<String> selectedFiles = new ArrayList<>();

    int id_category;
    int id_criterion = -1;
    int id_type;

    @SuppressLint("SimpleDateFormat") final SimpleDateFormat date = new SimpleDateFormat("dd MMM yyyy");
    String[] monthName = {"Янв", "Фев","Мар","Апр","Май","Июн","Июл","Авг","Сен","Окт","Ноя","Дек"};


    String TAG = "Upload fragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_upload));
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity)getActivity();
        categorySpinner = view.findViewById(R.id.upCategorySpinner);
        typeSpinner = view.findViewById(R.id.upTypeSpinner);

        dateEventPole = view.findViewById(R.id.upDateEventPole);
        datePublicationPole = view.findViewById(R.id.upDatePublicationPole);

        fileLoadBlock1 = view.findViewById(R.id.upFileLoadBlock1);
        fileLoadBlock2 = view.findViewById(R.id.upFileLoadBlock2);
        fileLoadBlock2.setVisibility(LinearLayout.GONE);

        fileLoadPole1 = view.findViewById(R.id.upFileLoadPole1);
        fileLoadPole2 = view.findViewById(R.id.upFileLoadPole2);
        nameText = view.findViewById(R.id.upNameText);

        radioButtons = view.findViewById(R.id.upRadioButtons);

        homeActivity.fragmentID = 3;

        Log.d(TAG, "Elements loaded");



        setCurrentTime();
        addListeners(view);

        if(homeActivity.categories==null)getCategories();


       // if(!homeActivity.uploadDataLoaded)getCategories();
        autoUpdate();
        if(id_criterion!=-1)radioButtons.check(id_criterion);
        Log.d(TAG, "clean");
        clean();

   //     if(homeActivity.update)updatePortfolio();
    }


    public void updatePortfolio(){
        Log.d(TAG, "update portfolio");
        Log.d(TAG, "old - " + nameText.getText().toString() );
        nameText.setText(homeActivity.portfolioDTO.getName());
        Log.d(TAG, "new - " + nameText.getText().toString() );
        dateEventPole.setText(homeActivity.portfolioDTO.getDate_event());
        datePublicationPole.setText(homeActivity.portfolioDTO.getDate_publication());

    }



    private void displayCategory(CategoryDTO categoryDTO){
        Log.d(TAG, "Display category");
        getCriteria(categoryDTO.getCategoryID());
        getTypes(categoryDTO.getCategoryID());
        id_category = categoryDTO.getCategoryID();
    }
    private void displayCriterion(CriterionDTO criterionDTO){
        id_criterion = criterionDTO.getCriterionID();
    }
    private void displayType(TypeDTO typeDTO){
        id_type = typeDTO.getTypeID();
     //   clean();
        if(homeActivity.update)updatePortfolio();

    }

    public void autoUpdate(){
        if(homeActivity.categories!=null)drawCategories();
        if(homeActivity.criteria!=null)drawCriteria();
        if(homeActivity.types!=null)drawTypes();

    }



    public void setCurrentTime(){
        Date currentTime = Calendar.getInstance().getTime();
        selectedTimeEvent.setTime(currentTime);
        selectedTimePublication.setTime(currentTime);
        dateEventPole.setText(date.format(currentTime));
        datePublicationPole.setText(date.format(currentTime));
    }


    public void addListeners(View view) {
        dateEventBlock = view.findViewById(R.id.upDateEventBlock);
        datePublicationBlock = view.findViewById(R.id.upDatePublicationBlock);
        fileLoadButton1 = view.findViewById(R.id.upFileLoadButton1);
        fileLoadButton2 = view.findViewById(R.id.upFileLoadButton2);
        openCloseButton = view.findViewById(R.id.upOpenCloseButton);
        uploadButton = view.findViewById(R.id.upUploadButton);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateEventBlock || v == datePublicationBlock)createDateBlock(v);
                if (v == fileLoadButton1 || v == fileLoadButton2)createOpenFileBlock(v);
                if (v == openCloseButton)openClose();
                if (v == uploadButton)createPortfolio();
            }
        };

        dateEventBlock.setOnClickListener(onClickListener);
        datePublicationBlock.setOnClickListener(onClickListener);
        fileLoadButton1.setOnClickListener(onClickListener);
        fileLoadButton2.setOnClickListener(onClickListener);
        openCloseButton.setOnClickListener(onClickListener);
        uploadButton.setOnClickListener(onClickListener);


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



        radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                if(checkedRadioButton != null){
                    if(checkedRadioButton.isChecked()){
                        id_criterion = checkedRadioButton.getId();
                        Log.d(TAG, "id_criterion = " + id_criterion);
                    }
                   // else checkedRadioButton.setChecked(true);
                    checkedRadioButton.setChecked(true);
                    Log.d(TAG, "currently checked - " + group.getCheckedRadioButtonId());

                }

            }
        });


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



        if(homeActivity.update)uploadButton.setText("Обновление портфолио");
        else uploadButton.setText("Загрузка портфолио");
    }



    public void createDateBlock(final View v){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        if (v == dateEventBlock){
                            dateEventPole.setText(day + " " + monthName[month] + " " + year);
                            selectedTimeEvent.set(Calendar.YEAR, year);
                            selectedTimeEvent.set(Calendar.MONTH, month);
                            selectedTimeEvent.set(Calendar.DAY_OF_MONTH, day);
                        }
                        if (v == datePublicationBlock){
                            datePublicationPole.setText(day + " " + monthName[month] + " " + year);
                            selectedTimePublication.set(Calendar.YEAR, year);
                            selectedTimePublication.set(Calendar.MONTH, month);
                            selectedTimePublication.set(Calendar.DAY_OF_MONTH, day);
                        }
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void createOpenFileBlock(final View v){
        OpenFileDialog openFileDialog = new OpenFileDialog(getContext())
                .setFilter(".*\\.(?:jpg|jpeg|png|doc|pdf)")
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(final String fileName) {
                        if(v == fileLoadButton1)
                        {
                            fileLoadPole1.setText(fileName);
                            fileLoadPole1.setSelection(fileLoadPole1.getText().length());
                        }
                        if(v == fileLoadButton2){
                            fileLoadPole2.setText(fileName);
                            fileLoadPole2.setSelection(fileLoadPole2.getText().length());
                        }
                        Toast.makeText(getContext().getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
                        selectedFiles.add(fileName);
                    }
                });
        openFileDialog.show();
    }



    public void openClose(){
        if(additionalFileBlock){
            fileLoadBlock2.setVisibility(LinearLayout.GONE);
            openCloseButton.setText("Добавить");
            fileLoadPole2.setText("...");
            if(selectedFiles.size() == 2) selectedFiles.remove(selectedFiles.size()-1);
            additionalFileBlock = false;
        }
        else{
            fileLoadBlock2.setVisibility(LinearLayout.VISIBLE);
            openCloseButton.setText("Убрать");
            additionalFileBlock = true;
        }
    }



    public void createPortfolio(){
        PortfolioDTO portfolio = new PortfolioDTO();

        String name = nameText.getText().toString();
        if(name.length() > 6){
            if(id_criterion != -1){
                portfolio.setName(name);

                portfolio.setId_category(id_category);
                portfolio.setId_criterion(id_criterion);
                portfolio.setId_type(id_type);

                portfolio.setDate_event(date.format(selectedTimeEvent.getTime()));
                portfolio.setDate_publication(date.format(selectedTimePublication.getTime()));

                sendPortfolio(portfolio);
            }
            else Toast.makeText(homeActivity,"Не выбран критерий получения",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(homeActivity,"Описание слишком короткое",Toast.LENGTH_SHORT).show();


    }


    public void sendPortfolio(PortfolioDTO portfolio){
        List<Object> postData = new ArrayList<>();
        TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));
        postData.add(token);
        postData.add(portfolio);

        Call<Integer> call;
        if(homeActivity.update){
            call = NetworkService.getInstance().getJSONApi().updatePortfolio(postData);
            portfolio.setId_portfolio(homeActivity.portfolioDTO.getId_portfolio());
        }
        else call = NetworkService.getInstance().getJSONApi().addPortfolio(postData);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body()!=null){
                    Log.d(TAG, "Портфолио загружено, id = " + response.body());
                    if(!selectedFiles.isEmpty())addFiles(response.body());

                    if(selectedFiles.isEmpty()){
                        Toast.makeText(homeActivity, "Портфолио загружено", Toast.LENGTH_LONG).show();
                        clean();
                    }
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
            }
        });
    }

    public void clean(){
        nameText.setText("");
        fileLoadPole1.setText("...");
        fileLoadPole2.setText("...");
        selectedFiles.clear();
    }



    public void addFiles(int id){
        if(!selectedFiles.isEmpty()){
            for(String fileName: selectedFiles){
                final File originalFile = new File(fileName);
                FileDTO file = new FileDTO(originalFile.getName(),"-",Functions.getFileExtension(originalFile),id);
                List<Object> postData = new ArrayList<>();
                TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));
                postData.add(token);
                postData.add(file);

                Call<String> call = NetworkService.getInstance().getJSONApi().addFile(postData);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body()!=null){
                            String answer = response.body();
                            Log.d(TAG, "Информация о файле загружена. Ссылка на него - " + answer );
                            uploadFiles(answer,originalFile);
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
            }
        }
    }

    public void uploadFiles(String src, final File originalFile){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),originalFile);
        Log.d(TAG,"Передача файла...");

        String username = Functions.getSharedUsername(homeActivity);
        String token = Functions.getSharedToken(homeActivity);
        RequestBody usernamePart = RequestBody.create(MultipartBody.FORM, username);
        RequestBody tokenPart = RequestBody.create(MultipartBody.FORM,token);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file",src,requestFile);
        Call<String> call = NetworkService.getInstance().getJSONApi().uploadFile(usernamePart,tokenPart, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.body());
                if(selectedFiles.size()!=0){
                    if(originalFile.getName().equals(new File(selectedFiles.get(selectedFiles.size()-1)).getName())){
                        Toast.makeText(homeActivity, "Портфолио загружено", Toast.LENGTH_LONG).show();
                        clean();
                        //clean
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Ошибка подключения");
            }
        });
    }



    public void drawCategories(){
        ArrayAdapter<CategoryDTO> adapter = new ArrayAdapter<CategoryDTO>(
                homeActivity,android.R.layout.simple_spinner_item, homeActivity.categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        if(homeActivity.update){
            for(int i = 0; i < adapter.getCount(); i++){
                if(adapter.getItem(i).getCategoryID() == homeActivity.portfolioDTO.getCategory()
                        .getCategoryID())categorySpinner.setSelection(i);
            }
        }
    }

    public void drawCriteria(){
        radioButtons.removeAllViews();



        boolean first = true;
        Log.d(TAG,"criteria size = " + homeActivity.criteria.size());
        RadioButton firstButton = null;
        Log.d(TAG, "[1] All buttons = " + radioButtons.getChildCount() + " criteria = " + homeActivity.criteria.size() );
        for(CriterionDTO criterion:homeActivity.criteria){
            RadioButton radioButton = new RadioButton(homeActivity);
            radioButton.setId(criterion.getCriterionID());
            radioButton.setText(criterion.getName_criterion());
            radioButtons.addView(radioButton);
            Log.d(TAG, "Radio buttons add");
            if (first){
                firstButton = radioButton;
                first = false;
               // radioButtons.check(radioButton.getId());
            //    id_criterion = radioButton.getId();
          //      radioButton.setChecked(true);
                Log.d(TAG, "first detected id = " + id_criterion);
            }
          //  first = false;
        }

        Log.d(TAG, "[2] All buttons = " + radioButtons.getChildCount() + " criteria = " + homeActivity.criteria.size() );
    //    radioButtons.check(radioButtons.getChildCount()-1);
    //    radioButtons.check(firstButton.getId());
   //     firstButton.setChecked(true);
    //    id_criterion = firstButton.getId();


    }

    public void drawTypes(){
        ArrayAdapter<TypeDTO> adapter = new ArrayAdapter<TypeDTO>(
                homeActivity,android.R.layout.simple_spinner_item, homeActivity.types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        if(homeActivity.update){
            for(int i = 0; i < adapter.getCount(); i++){
                if(adapter.getItem(i).getTypeID() == homeActivity.portfolioDTO.getType()
                        .getTypeID())typeSpinner.setSelection(i);
            }
        }
    }





    public void getCategories(){
     //   Log.d(TAG, "new[2] - " + nameText.getText().toString() );
        Call<List<CategoryDTO>> call = NetworkService.getInstance().getJSONApi().getCategories();
        call.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if(response.body()!=null){
             //       Log.d(TAG, "new[3] - " + nameText.getText().toString() );
                    homeActivity.categories = response.body();
                    Log.d(TAG, "Категорий получено: " + homeActivity.categories.size());
                    drawCategories();

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
                    homeActivity.criteria = response.body();
                    Log.d(TAG, "Критериев получено: " + homeActivity.criteria.size());

                    drawCriteria();



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
                    homeActivity.types = response.body();
                    Log.d(TAG, "Типов участия получено:" + homeActivity.types.size());

                    drawTypes();


                }
            }
            @Override
            public void onFailure(Call<List<TypeDTO>> call, Throwable t) {
                Log.d(TAG, "Ошибка получения типов участия");
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Detach");
        clean();
        homeActivity.update = false;
    }
}
