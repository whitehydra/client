package com.fadeev.bgtu.client.retrofit;

import com.fadeev.bgtu.client.Constants;
import com.fadeev.bgtu.client.dto.AuthorizationDTO;
import com.fadeev.bgtu.client.dto.CategoryDTO;
import com.fadeev.bgtu.client.dto.CriterionDTO;
import com.fadeev.bgtu.client.dto.FacultyDTO;
import com.fadeev.bgtu.client.dto.FileDTO;
import com.fadeev.bgtu.client.dto.GroupDTO;
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

    @POST(Constants.URL.VERSION + Constants.URL.AUTH)
    Call<UserDTO> getUser(@Body TokenAndNameDTO data);

    @GET(Constants.URL.VERSION + Constants.URL.ACCESS_RECOVERY)
    Call<String> accessRecovery (@Path("username") String username);

    @GET(Constants.URL.VERSION + Constants.URL.PIN_RECOVERY)
    Call<String> pinRecovery (@Path("username") String username);


    @POST(Constants.URL.VERSION + Constants.URL.USERS)
    Call<List<UserDTO>> getUsersList(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.LOGIN)
    Call<String> postAuthorizationGetToken(@Body AuthorizationDTO data);

    @Multipart
    @POST(Constants.URL.VERSION + Constants.URL.UPLOAD_AVATAR)
    Call<String> uploadAvatar(
            @Part("username") RequestBody username,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part file);

    @Multipart
    @POST(Constants.URL.VERSION + Constants.URL.UPLOAD_FILE)
    Call<String> uploadFile(@Part("username") RequestBody username,
                            @Part("token") RequestBody token,
                            @Part MultipartBody.Part file);

    @POST(Constants.URL.VERSION + Constants.URL.ADD_PORTFOLIO)
    Call<Integer> addPortfolio(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.GET_PORTFOLIO)
    Call<List<PortfolioDTO>> getPortfolioList(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.GET_USER_PORTFOLIO)
    Call<List<PortfolioDTO>> getUserPortfolioList (@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.UPDATE_PORTFOLIO)
    Call<Integer> updatePortfolio(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.DELETE_PORTFOLIO)
    Call<Integer> deletePortfolio(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.ADD_FILE_INFO)
    Call<String> addFile(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.GET_FILE_INFO)
    Call<List<FileDTO>> getFile(@Body List<Object> list);

    @GET(Constants.URL.VERSION + Constants.URL.GET_CATEGORIES)
    Call<List<CategoryDTO>> getCategories();

    @GET(Constants.URL.VERSION + Constants.URL.GET_CRITERIA)
    Call<List<CriterionDTO>> getCriteria(@Path("id") int id);

    @GET(Constants.URL.VERSION + Constants.URL.GET_TYPE)
    Call<List<TypeDTO>> getTypes(@Path("id") int id);

    @GET(Constants.URL.VERSION + Constants.URL.GET_FACULTIES)
    Call<List<FacultyDTO>> getFaculties();

    @GET(Constants.URL.VERSION + Constants.URL.GET_GROUPS)
    Call<List<GroupDTO>> getGroups(@Path("id") int id);

    @GET(Constants.URL.VERSION + Constants.URL.GET_PIN_STATUS)
    Call<Boolean> getPinStatus(@Path("username") String username);

    @POST(Constants.URL.VERSION + Constants.URL.CHECK_PIN)
    Call<Boolean> checkPin(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.SET_PIN)
    Call<Boolean> setPin(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.EDIT_PROFILE)
    Call<Boolean> editProfile(@Body List<Object> list);

    @POST(Constants.URL.VERSION + Constants.URL.EDIT_PASSWORD)
    Call<Boolean> editPassword(@Body List<Object> list);
}


