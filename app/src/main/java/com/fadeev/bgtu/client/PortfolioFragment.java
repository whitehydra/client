package com.fadeev.bgtu.client;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fadeev.bgtu.client.dto.FileDTO;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.retrofit.NetworkService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioFragment extends Fragment {
    String TAG = "Portfolio fragment";

    HomeActivity homeActivity;

    TextView nameText;
    TextView categoryText;
    TextView criterionText;
    TextView typeText;
    TextView dateEventText;
    TextView datePublicationText;
    TextView fileNameText1;
    TextView fileTypeText1;
    TextView fileNameText2;
    TextView fileTypeText2;

    Button loadFileButton1;
    Button loadFileButton2;
    Button updatePortfolioButton;

    LinearLayout fileBlock1;
    LinearLayout fileBlock2;

    List<FileDTO> fileList;
    DownloadManager downloadManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_portfolio));
        return inflater.inflate(R.layout.fragment_portfolio, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        homeActivity = (HomeActivity)getActivity();
        nameText = view.findViewById(R.id.pflNamePole);
        categoryText = view.findViewById(R.id.pflCategoryPole);
        criterionText = view.findViewById(R.id.pflCriterionPole);
        typeText = view.findViewById(R.id.pflTypePole);
        dateEventText = view.findViewById(R.id.pflDateEventPole);
        datePublicationText = view.findViewById(R.id.pflDatePublicationPole);
        fileNameText1 = view.findViewById(R.id.pflFileNamePole1);
        fileTypeText1 = view.findViewById(R.id.pflFileTypePole1);
        fileNameText2 = view.findViewById(R.id.pflFileNamePole2);
        fileTypeText2 = view.findViewById(R.id.pflFileTypePole2);

        loadFileButton1 = view.findViewById(R.id.pflLoadFileButton1);
        loadFileButton2 = view.findViewById(R.id.pflLoadFileButton2);
        updatePortfolioButton = view.findViewById(R.id.pflUpdatePortfolioButton);
        if(homeActivity.userView)updatePortfolioButton.setVisibility(View.INVISIBLE);

        fileBlock1 = view.findViewById(R.id.pflFileBlock1);
        fileBlock2 = view.findViewById(R.id.pflFileBlock2);

        fileBlock1.setVisibility(LinearLayout.GONE);
        fileBlock2.setVisibility(LinearLayout.GONE);

        homeActivity.fragmentID = 4;

        printData();
        loadFilesInfo();
    }

    public void loadListener(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == loadFileButton1)downloadFile(0);
                if(v == loadFileButton2)downloadFile(1);
                if(v == updatePortfolioButton){
                    homeActivity.update = true;
                    homeActivity.fragmentManager.beginTransaction().replace(R.id.homeFrame, homeActivity.uploadFragment).commit();
                }
            }
        };
        loadFileButton1.setOnClickListener(onClickListener);
        loadFileButton2.setOnClickListener(onClickListener);
        updatePortfolioButton.setOnClickListener(onClickListener);
    }

    public void downloadFile(int num){
        downloadManager = (DownloadManager)homeActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Functions.createDownloadUri(homeActivity,fileList.get(num).getFile_src()));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileList.get(num).getFile_name());
        downloadManager.enqueue(request);
    }

    public void printData(){
        nameText.setText(homeActivity.portfolioDTO.getName());
        categoryText.setText(homeActivity.portfolioDTO.getCategory().getName_category());
        criterionText.setText(homeActivity.portfolioDTO.getCriterion().getName_criterion());
        typeText.setText(homeActivity.portfolioDTO.getType().getName_type());
        dateEventText.setText(homeActivity.portfolioDTO.getDate_event());
        datePublicationText.setText(homeActivity.portfolioDTO.getDate_publication());
    }

    public void printFileData(){
        if (!fileList.isEmpty()){
            fileBlock1.setVisibility(LinearLayout.VISIBLE);
            fileNameText1.setText(fileList.get(0).getFile_name());
            fileTypeText1.setText(fileList.get(0).getFile_type());
            if(fileList.size() == 2){
                fileBlock2.setVisibility(LinearLayout.VISIBLE);
                fileNameText2.setText(fileList.get(1).getFile_name());
                fileTypeText2.setText(fileList.get(1).getFile_type());
            }
        }
    }

    public void loadFilesInfo(){
        List<Object> postData = new ArrayList<>();
        TokenAndNameDTO token = new TokenAndNameDTO(Functions.getSharedUsername(homeActivity),Functions.getSharedToken(homeActivity));
        HashMap<String, Integer> portfolioID = new HashMap<>();
        portfolioID.put("id_portfolio",homeActivity.portfolioDTO.getId_portfolio());
        postData.add(token);
        postData.add(portfolioID);

        Call<List<FileDTO>> call = NetworkService.getInstance().getJSONApi().getFile(postData);
        call.enqueue(new Callback<List<FileDTO>>() {
            @Override
            public void onResponse(Call<List<FileDTO>> call, Response<List<FileDTO>> response) {
                fileList = response.body();
                loadListener();
                printFileData();
            }
            @Override
            public void onFailure(Call<List<FileDTO>> call, Throwable t) { }
        });
    }
}
