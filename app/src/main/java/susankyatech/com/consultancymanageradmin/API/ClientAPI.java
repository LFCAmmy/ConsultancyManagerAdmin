package susankyatech.com.consultancymanageradmin.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import susankyatech.com.consultancymanageradmin.Model.Login;
import susankyatech.com.consultancymanageradmin.Model.UniversityResponse;

public interface ClientAPI {

    @GET("students/client/{id}")
    Call<Login> getClientDetail(@Path("id") int id);

    @GET("university/index")
    Call<UniversityResponse> getUniversity();

}
