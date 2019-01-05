package susankyatech.com.consultancymanageradmin.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.BuildConfig;
import susankyatech.com.consultancymanageradmin.Generic.Client;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Login.StudentLoginFragment;
import susankyatech.com.consultancymanageradmin.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        switch (BuildConfig.FLAVOR)
        {
            case Client
                    .ACHIEVERS:
                App.db().putInt(Keys.CLIENT_ID, 5);
//                App.db().putInt(Keys.CLIENT_ID, 2);
                break;

            case Client.RATIONAL:
                App.db().putInt(Keys.CLIENT_ID, 31);
                break;

            default:
                App.db().putInt(Keys.CLIENT_ID, 2);
        }
        if (isAccountLoggedIn()) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.login_container, new StudentLoginFragment()).commit();
    }

    private boolean isAccountLoggedIn() {
        return App.db().getBoolean(Keys.USER_LOGGED_IN);
    }
}
