package susankyatech.com.consultancymanageradmin.API;

import android.support.v7.widget.CardView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import susankyatech.com.consultancymanageradmin.Model.Login;

public interface StudentAPI {

    @FormUrlEncoded
    @POST("students/change-primary-info")
    Call<Login> changePrimaryInfo(@Field("email") String email,
                                  @Field("name") String name,
                                  @Field("address") String address,
                                  @Field("phone") String phone,
                                  @Field("dob") String dob,
                                  @Field("gender") String gender);

    @FormUrlEncoded
    @POST("students/detail")
    Call<Login> saveDetail(@Field("qualification") String qualification,
                           @Field("father_name") String fatherName,
                           @Field("mother_name") String motherName,
                           @Field("student_id") int student_id,
                           @Field("interested_country") String interested_country,
                           @Field("interested_course") String interested_course,
                           @Field("secondary_phone") String secondary_phone,
                           @Field("secondary_address") String secondary_address);

    @FormUrlEncoded
    @POST("students/verify-student")
    Call<Login> verifyStudent(@Field("dob") String dob,
                              @Field("code") String code);

    @Multipart
    @POST("students/save-document/")
    Call<ResponseBody> addFiles(@Part("group") RequestBody group,
                         @Part("description") RequestBody description,
                         @Part("document_name") RequestBody document_name,
                         @Part MultipartBody.Part document,
                         @Part("student_id") RequestBody student_id);

    @Multipart
    @POST("students/profile-image/")
    Call<ResponseBody> addProfilePic(@Part("student_id") RequestBody student_id,
                                     @Part MultipartBody.Part profile_image);
}
