package com.fadeev.bgtu.client.retrofit;

import com.fadeev.bgtu.client.dto.AuthorizationDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JSONPlaceHolderApi {


    @POST("/authentication")
    Call<UserDTO> postTokenGetUser(@Body TokenAndNameDTO data);

    @POST("/login")
    Call<String> postAuthorizationGetToken(@Body AuthorizationDTO data);
}


