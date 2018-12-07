package susankyatech.com.consultancymanageradmin.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import susankyatech.com.consultancymanageradmin.Fragment.StudentLoginFragment;
import susankyatech.com.consultancymanageradmin.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.login_container, new StudentLoginFragment()).commit();
    }
}
