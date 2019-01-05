package susankyatech.com.consultancymanageradmin.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import susankyatech.com.consultancymanageradmin.Model.Login;

public interface LoginAPI {

    @FormUrlEncoded
    @POST("students/login")
    Call<Login> studentLogin(@Field("email") String userEmail,
                             @Field("password") String password,
                             @Field("client_id") int clientId);

    @FormUrlEncoded
    @POST("students/register")
    Call<Login> studentRegister(@Field("name") String username,
                                @Field("email") String userEmail,
                                @Field("phone") String userPhone,
                                @Field("password") String password,
                                @Field("address") String userAddress,
                                @Field("gender") String userGender,
                                @Field("dob") String userDob,
                                @Field("client_id")int clientId);

}
