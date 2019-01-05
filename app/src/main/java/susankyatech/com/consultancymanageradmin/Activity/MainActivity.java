package susankyatech.com.consultancymanageradmin.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import susankyatech.com.consultancymanageradmin.API.StudentAPI;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Appointment.AppointmentFragment;
import susankyatech.com.consultancymanageradmin.BuildConfig;
import susankyatech.com.consultancymanageradmin.Chat.ChatFragment;
import susankyatech.com.consultancymanageradmin.FileTransfer.FileTransferFragment;
import susankyatech.com.consultancymanageradmin.ConsultancyProfile.ConsultancyProfileFragment;
import susankyatech.com.consultancymanageradmin.FileTransfer.UserVerificationFragment;
import susankyatech.com.consultancymanageradmin.Generic.Client;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Model.Data;
import susankyatech.com.consultancymanageradmin.Model.Login;
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

    private EditText userName, year, month, day, stream, fatherName, motherName, userEmail, userAddress, userPhone, destination;
    private EditText interestedCourse, secondaryPhone, secondaryAddress;

    private Spinner qualificationSpinner, gender;
    private String selectedLevel, selectedUserGender;
    private int mYear, mMonth, mDay;

    private String[] qualificationList = {
            "+2",
            "Bachelors",
            "Masters"
    };
    private String[] sex = {"Male", "Female"};

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Data data;

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

        data = App.db().getObject(FragmentKeys.DATA, Data.class);

        if (data.detail == null) {
            getStudentFurtherDetails();
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ConsultancyProfileFragment()).commit();

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.inflateMenu(R.menu.nav_menu_student);

        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.inflateHeaderView(R.layout.nav_header_layout);
        TextView userName = navView.findViewById(R.id.user_name);
        TextView userEmail = navView.findViewById(R.id.user_email);
        CircleImageView userImage = navView.findViewById(R.id.icon);

        userName.setText(data.name);
        userEmail.setText(data.email);
        Picasso.get().load(data.profile_image).placeholder(R.drawable.user).into(userImage);
    }

    private void getStudentFurtherDetails() {
        final MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("Complete your Profile")
                .customView(R.layout.get_student_detail_layout, true)
                .positiveText("Save Details")
                .negativeText("Close")
                .positiveColor(getResources().getColor(R.color.green))
                .negativeColor(getResources().getColor(R.color.red))
                .show();

        userName = materialDialog.getCustomView().findViewById(R.id.enquiry_name);
        year = materialDialog.getCustomView().findViewById(R.id.year);
        month = materialDialog.getCustomView().findViewById(R.id.month);
        day = materialDialog.getCustomView().findViewById(R.id.day);
        gender = materialDialog.getCustomView().findViewById(R.id.student_gender);
        qualificationSpinner = materialDialog.getCustomView().findViewById(R.id.qualification_spinner);
        stream = materialDialog.getCustomView().findViewById(R.id.enquiry_level_completed);
        fatherName = materialDialog.getCustomView().findViewById(R.id.enquiry_father_name);
        motherName = materialDialog.getCustomView().findViewById(R.id.enquiry_mother_name);

        userAddress = materialDialog.getCustomView().findViewById(R.id.enquiry_address);
        userEmail = materialDialog.getCustomView().findViewById(R.id.enquiry_email);
        userPhone = materialDialog.getCustomView().findViewById(R.id.enquiry_phone);

        destination = materialDialog.getCustomView().findViewById(R.id.enquiry_interested_country);
        interestedCourse = materialDialog.getCustomView().findViewById(R.id.enquiry_interested_course);
        secondaryPhone = materialDialog.getCustomView().findViewById(R.id.enquiry_secondary_phone);
        secondaryAddress = materialDialog.getCustomView().findViewById(R.id.enquiry_secondary_address);

        ArrayAdapter levelAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, qualificationList);
        ArrayAdapter genderAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sex);

        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userEmail.setText(data.email);
        userName.setText(data.name);
        userPhone.setText(data.phone);
        userAddress.setText(data.address);

        String[] newDob = data.dob.split("-");
        year.setText(newDob[0]);
        month.setText(newDob[1]);
        day.setText(newDob[2]);

        qualificationSpinner.setAdapter(levelAdapter);
        gender.setAdapter(genderAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUserGender = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        qualificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLevel = qualificationList[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        materialDialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFurtherDetails(materialDialog);

            }
        });
        materialDialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });
    }

    private void addFurtherDetails(final MaterialDialog materialDialog) {
        final String name = userName.getText().toString();
        String dobYear = year.getText().toString();
        String dobMth = month.getText().toString();
        String dobDay = day.getText().toString();
        final String studentQualification = stream.getText().toString();
        final String userFather = fatherName.getText().toString();
        final String userMother = motherName.getText().toString();

        final String phone = userPhone.getText().toString();
        final String email = userEmail.getText().toString();
        final String address = userAddress.getText().toString();

        final String userDestination = destination.getText().toString();
        final String userCourse = interestedCourse.getText().toString();
        final String userSecPhone = secondaryPhone.getText().toString();
        final String userSecAddress = secondaryAddress.getText().toString();

        if (TextUtils.isEmpty(studentQualification)) {
            stream.setError("Enter your qualification");
            stream.requestFocus();
        } else if (TextUtils.isEmpty(dobYear)) {
            year.setError("Enter year");
            year.requestFocus();
        } else if (TextUtils.isEmpty(dobMth)) {
            month.setError("Enter month");
            month.requestFocus();
        } else if (TextUtils.isEmpty(dobDay)) {
            day.setError("Enter day");
            day.requestFocus();
        } else {
            final String studentDOB = dobYear + "-" + dobMth + "-" + dobDay;
            String studentCourseCompleted = selectedLevel + "," + studentQualification;
            StudentAPI studentAPI = App.consultancyRetrofit().create(StudentAPI.class);
            studentAPI.saveDetail(studentCourseCompleted, userFather, userMother, App.db().getInt(Keys.USER_ID), userDestination, userCourse, userSecPhone, userSecAddress)
                    .enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    editStudentPrimaryInfo(name, email, address, phone, materialDialog, studentDOB);
                                }
                            } else {
                                try {
                                    Log.d("client", "onResponse: error" + response.errorBody().string());
                                    MDToast mdToast = MDToast.makeText(MainActivity.this, "There was something wrong while saving your info. Please try again!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                                    mdToast.show();
                                } catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {

                        }
                    });
        }
    }

    private void editStudentPrimaryInfo(String name, String email, String address, String phone, final MaterialDialog materialDialog, String studentDOB) {
        StudentAPI studentAPI = App.consultancyRetrofit().create(StudentAPI.class);
        studentAPI.changePrimaryInfo(email, name, address, phone, studentDOB, selectedUserGender)
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                App.db().putObject(FragmentKeys.DATA, response.body().data);
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                MDToast mdToast = MDToast.makeText(MainActivity.this, "Your info is successfully saved!", Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                                mdToast.show();
                                materialDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ChatFragment()).addToBackStack(null).commit();
                break;
            case R.id.call:
                String phoneNo = "014106744";
                Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_SHORT).show();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.fromParts("tel", phoneNo, null));
                startActivity(phoneIntent);
        }
        return true;
    }

    private void userMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ConsultancyProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.profile:
                if (data.detail == null) {
                    getStudentFurtherDetails();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new StudentProfileFragment()).addToBackStack(null).commit();
                }
                break;
            case R.id.notice:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new NoticeListFragment()).addToBackStack(null).commit();
            break;
            case R.id.appointment:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new AppointmentFragment()).addToBackStack(null).commit();
                break;
            case R.id.file_transfer:
                if (App.db().getBoolean(Keys.IS_VERIFIED)){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new FileTransferFragment()).addToBackStack(null).commit();
                }else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new UserVerificationFragment()).addToBackStack(null).commit();
                }
                break;
            case R.id.logout:
                App.logOut(this);
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
