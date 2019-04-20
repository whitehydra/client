package com.fadeev.bgtu.client.retrofit;

import com.fadeev.bgtu.client.dto.AuthorizationDTO;
import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.CriterionDTO;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.TypeDTO;
import com.fadeev.bgtu.client.dto.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface JSONPlaceHolderApi {


    @POST("/authentication")
    Call<UserDTO> postTokenGetUser(@Body TokenAndNameDTO data);

    @POST("/login")
    Call<String> postAuthorizationGetToken(@Body AuthorizationDTO data);

    @Multipart
    @POST("/file")
    Call<String> uploadFile(
            @Part("text") RequestBody info,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("/file")
    Call<String> uploadAvatar(
            @Part("username") RequestBody username,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part file);



    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithUrl(@Url String fileUrl);

    @POST("/portfolio/files")
    Call<String> addFile(@Body List<Object> list);

    @POST("/portfolio/add")
    Call<Integer> addPortfolio(@Body List<Object> list);


    @GET("/categories")
    Call<List<CategoryDTO>> getCategories();

    @GET("/criteria/{id}")
    Call<List<CriterionDTO>> getCriteria(@Path("id") int id);

    @GET("/types/{id}")
    Call<List<TypeDTO>> getTypes(@Path("id") int id);



}


