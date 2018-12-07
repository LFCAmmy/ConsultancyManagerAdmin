package susankyatech.com.consultancymanageradmin.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.Chat.ChatFragment;
import susankyatech.com.consultancymanageradmin.FileTransfer.FileTransferFragment;
import susankyatech.com.consultancymanageradmin.ConsultancyProfile.ConsultancyProfileFragment;
import susankyatech.com.consultancymanageradmin.Notice.NoticeListFragment;
import susankyatech.com.consultancymanageradmin.Profile.StudentProfileFragment;
import susankyatech.com.consultancymanageradmin.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawable_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_app_bar)
    Toolbar mToolbar;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Consultancy Manager");

        }
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ConsultancyProfileFragment()).commit();

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.inflateMenu(R.menu.nav_menu_student);

        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.inflateHeaderView(R.layout.nav_header_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ChatFragment()).commit();
                break;
        }
        return true;
    }

    private void userMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ConsultancyProfileFragment()).commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new StudentProfileFragment()).commit();
                break;
            case R.id.notice:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new NoticeListFragment()).commit();
            break;
            case R.id.file_transfer:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new FileTransferFragment()).commit();
                break;
        }
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        userMenuSelector(item);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
