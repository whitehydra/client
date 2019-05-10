package com.fadeev.bgtu.client.retrofit;

import com.fadeev.bgtu.client.Constants;
import com.fadeev.bgtu.client.dto.AuthorizationDTO;
import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.CriterionDTO;
import com.fadeev.bgtu.client.dto.FileDTO;
import com.fadeev.bgtu.client.dto.PortfolioDTO;
import com.fadeev.bgtu.client.dto.TokenAndNameDTO;
import com.fadeev.bgtu.client.dto.TypeDTO;
import com.fadeev.bgtu.client.dto.UserDTO;

import java.util.List;
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
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface JSONPlaceHolderApi {
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithUrl(@Url String fileUrl);

    @POST(Constants.URL.AUTH)
    Call<UserDTO> postTokenGetUser(@Body TokenAndNameDTO data);

    @POST(Constants.URL.LOGIN)
    Call<String> postAuthorizationGetToken(@Body AuthorizationDTO data);

    @Multipart
    @POST(Constants.URL.UPLOAD_AVATAR)
    Call<String> uploadAvatar(
            @Part("username") RequestBody username,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part file);

    @Multipart
    @POST(Constants.URL.UPLOAD_FILE)
    Call<String> uploadFile(@Part("username") RequestBody username,
                            @Part("token") RequestBody token,
                            @Part MultipartBody.Part file);

    @POST(Constants.URL.ADD_PORTFOLIO)
    Call<Integer> addPortfolio(@Body List<Object> list);

    @POST(Constants.URL.GET_PORTFOLIO)
    Call<List<PortfolioDTO>> getPortfolioList(@Body List<Object> list);

    @POST(Constants.URL.UPDATE_PORTFOLIO)
    Call<Integer> updatePortfolio(@Body List<Object> list);

    @POST(Constants.URL.DELETE_PORTFOLIO)
    Call<Integer> deletePortfolio(@Body List<Object> list);

    @POST(Constants.URL.ADD_FILE_INFO)
    Call<String> addFile(@Body List<Object> list);

    @POST(Constants.URL.GET_FILE_INFO)
    Call<List<FileDTO>> getFile(@Body List<Object> list);

    @GET(Constants.URL.GET_CATEGORIES)
    Call<List<CategoryDTO>> getCategories();

    @GET(Constants.URL.GET_CRITERIA)
    Call<List<CriterionDTO>> getCriteria(@Path("id") int id);

    @GET(Constants.URL.GET_TYPE)
    Call<List<TypeDTO>> getTypes(@Path("id") int id);


    @GET(Constants.URL.GET_PIN_STATUS)
    Call<Boolean> getPinStatus(@Path("username") String username);

    @POST(Constants.URL.CHECK_PIN)
    Call<Boolean> checkPin(@Body List<Object> list);

    @POST(Constants.URL.SET_PIN)
    Call<Boolean> setPin(@Body List<Object> list);
}


