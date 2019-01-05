package susankyatech.com.consultancymanageradmin.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import susankyatech.com.consultancymanageradmin.Model.Login;
import susankyatech.com.consultancymanageradmin.Model.NoticeResponse;

public interface NoticeAPI {

    @GET("students/notices/")
    Call<NoticeResponse> getNotices();
}
