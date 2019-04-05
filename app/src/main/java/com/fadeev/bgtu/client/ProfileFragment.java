package com.fadeev.bgtu.client;

import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fadeev.bgtu.client.dto.UserDTO;


public class ProfileFragment extends Fragment {
    HomeActivity homeActivity;
    TextView pfNameValue;
    TextView pfPositionValue;
    TextView pfFacultyValue;
    TextView pfGroupValue;
    TextView pfNumberValue;
    TextView pfMailValue;
    TextView pfInfoValue;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    final String LOG_TAG = "profileFragmentLogs ";

   // private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "Fragment onCreate");


//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Fragment1 onCreateView");
        getActivity().setTitle(getString(R.string.title_profile));


//        homeActivity.userLoginTask = new HomeActivity.UserLoginTask("admin", "admin");
//        homeActivity.userLoginTask.execute((Void) null);

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        pfNameValue = getView().findViewById(R.id.pfNameValue);
        pfPositionValue = getView().findViewById(R.id.pfPositionValue);
        pfFacultyValue = getView().findViewById(R.id.pfFacultyValue);
        pfGroupValue = getView().findViewById(R.id.pfGroupValue);
        pfNumberValue = getView().findViewById(R.id.pfNumberValue);
        pfMailValue = getView().findViewById(R.id.pfMailValue);
        pfInfoValue = getView().findViewById(R.id.pfInfoValue);


        homeActivity = (HomeActivity)getActivity();
        homeActivity.loginExecute("admin", "admin",1);

      //  pfNameValue.setText(homeActivity.userDTO.getUsername());
    }

    public void loadData(){
        pfNameValue.setText("loading...");
    }

    public void printData(){
        pfNameValue.setText(homeActivity.userDTO.getName());
        pfPositionValue.setText(homeActivity.userDTO.getLevel());
        pfFacultyValue.setText(homeActivity.userDTO.getFaculty());
        pfGroupValue.setText(homeActivity.userDTO.getStudyGroup());
        pfNumberValue.setText(homeActivity.userDTO.getPhone());
        pfMailValue.setText(homeActivity.userDTO.getMail());
        pfInfoValue.setText(homeActivity.userDTO.getInfo());

    }





    @Override
    public void onStart(){
        super.onStart();
        Log.d(LOG_TAG, "Fragment1 onStart");

//        while(homeActivity.userDTO==null){
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        pfNameValue.setText(homeActivity.userDTO.getUsername());

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "Fragment1 onResume");

//        while(homeActivity.userDTO==null){
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        pfNameValue.setText(homeActivity.userDTO.getUsername());

    }



//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(LOG_TAG, "Fragment1 onAttach");

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
