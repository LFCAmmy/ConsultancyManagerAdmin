package susankyatech.com.consultancymanageradmin.Application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import susankyatech.com.consultancymanageradmin.Activity.LoginActivity;
import susankyatech.com.consultancymanageradmin.Generic.Client;
import susankyatech.com.consultancymanageradmin.Generic.Keys;

public class App extends MultiDexApplication {
    public static String BASE_URL = Client.HTTPS + Client.BASE_URL_API;
    public static String RATIONAL_BASE_URL = Client.HTTPS + "rational.herokuapp.com/api/";
    public static TinyDB tinyDB;

    @Override
    public void onCreate() {
        super.onCreate();
        tinyDB = new TinyDB(this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public static TinyDB db() {
        return tinyDB;
    }

    public static Retrofit consultancyRetrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        if (App.db().getBoolean(Keys.USER_LOGGED_IN)) {
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);
            okHttpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + App.db().getString(Keys.USER_TOKEN))
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            builder.client(okHttpBuilder.build());
        }
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static Retrofit rationalRetrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(RATIONAL_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES);
//        if (App.db().getBoolean(Keys.USER_LOGGED_IN)) {
//            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
//                    .connectTimeout(5, TimeUnit.MINUTES)
//                    .readTimeout(5, TimeUnit.MINUTES);
//            okHttpBuilder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//                    Request request = original.newBuilder()
//                            .header("Authorization", "Bearer " + App.db().getString(Keys.USER_TOKEN))
//                            .method(original.method(), original.body())
//                            .build();
//
//                    return chain.proceed(request);
//                }
//            });
//            builder.client(okHttpBuilder.build());
//        }
        builder.client(okHttpBuilder.build());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static Retrofit noHeaderConsultancyRetrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static void logOut(Activity activity) {
        App.db().clear(); //Removes all the content of SharedPreferences
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
