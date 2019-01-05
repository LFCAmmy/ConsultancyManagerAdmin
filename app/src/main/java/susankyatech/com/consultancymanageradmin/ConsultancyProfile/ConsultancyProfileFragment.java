package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.security.Key;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import susankyatech.com.consultancymanageradmin.API.ClientAPI;
import susankyatech.com.consultancymanageradmin.API.StudentAPI;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Adapter.ConsultancyProfileViewPagerAdapter;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.BuildConfig;
import susankyatech.com.consultancymanageradmin.Generic.Client;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Model.Data;
import susankyatech.com.consultancymanageradmin.Model.Login;
import susankyatech.com.consultancymanageradmin.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultancyProfileFragment extends Fragment {

    @BindView(R.id.profile_tabs)
    TabLayout tabLayout;
    @BindView(R.id.profile_viewpager)
    ViewPager viewPager;
    @BindView(R.id.profile_banner)
    ImageView banner;
    @BindView(R.id.consultancy_logo)
    CircleImageView logo;
    @BindView(R.id.consultancy_name)
    TextView name;
    @BindView(R.id.consultancy_email)
    TextView email;
    @BindView(R.id.sendInquiry)
    RelativeLayout sendInquiry;
    @BindView(R.id.whatsApp)
    FloatingActionButton whatsApp;
    @BindView(R.id.messenger)
    FloatingActionButton messenger;
    @BindView(R.id.viber)
    FloatingActionButton viber;

    private Data data;

    private EditText userName, year, month, day, stream, fatherName, motherName, userEmail, userAddress, userPhone, destination;
    private EditText interestedCourse, secondaryPhone, secondaryAddress;

    private Spinner qualificationSpinner, gender;
    private String selectedLevel, selectedUserGender;
    private int mYear, mMonth, mDay;
    String mobileNo = "+9779803228034";


    private String[] qualificationList = {
            "+2",
            "Bachelors",
            "Masters"
    };
    private String[] sex = {"Male", "Female"};

    public ConsultancyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultancy_profile, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");
        init();
        return view;
    }

    private void init() {
        getClientProfileInfo();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    return;
                } else {
                    sendUserToMapFragment();
                }
            }
        });

        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(mobileNo) + "@s.whatsapp.net");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "Please Install WhatsApp", Toast.LENGTH_LONG).show();
                }

            }
        });

        viber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (!isViberClientInstalled(getContext())) {
                        //goToMarket(context,"\"https://play.google.com/store/apps/details?id=com.viber.voip\"");
                        goToViberMarket(getContext());
                        return;
                    }

                    Uri uri = Uri.parse("tel:" + Uri.encode(mobileNo));
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
                    intent.setData(uri);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Please Install Viber Messenger", Toast.LENGTH_LONG).show();
                }
            }
        });

        messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messengerLink = "/rationaledu/";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setPackage("com.facebook.orca");
                sendIntent.setData(Uri.parse("https://m.me/" + messengerLink));
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                }
            }
        });

        sendInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = App.db().getObject(FragmentKeys.DATA, Data.class);
                if (data.detail == null) {
                    getStudentFurtherDetails();
                } else {
                    getEnquiry();
                }
            }
        });
    }


    public static boolean isViberClientInstalled(Context context) {
        PackageManager myPackageMgr = context.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.viber.voip", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }

    public static void goToViberMarket(Context ctx) {

        Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=com.viber.voip");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(myIntent);

        return;
    }

    private void getEnquiry() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(FragmentKeys.ISCONSULTANCY, true);
        FragmentTransaction fragmentTransaction = ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction();
        OpenInquirySelectCountryFragment openInquirySelectCountryFragment = new OpenInquirySelectCountryFragment();
        openInquirySelectCountryFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, openInquirySelectCountryFragment).addToBackStack(null).commit();
    }

    private void getStudentFurtherDetails() {
        final MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
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

        ArrayAdapter levelAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, qualificationList);
        ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, sex);

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
                                    MDToast mdToast = MDToast.makeText(getContext(), "There was something wrong while saving your info. Please try again!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
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
                                startActivity(new Intent(getContext(), MainActivity.class));
                                MDToast mdToast = MDToast.makeText(getContext(), "Your info is successfully saved!", Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
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

    private void sendUserToMapFragment() {
        FragmentTransaction fragmentTransaction = ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction();
        ShowMapFragment showMapFragment = new ShowMapFragment();
        fragmentTransaction.replace(R.id.main_container, showMapFragment).addToBackStack(null).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                sendUserToMapFragment();
            }
        }
    }

    private void getClientProfileInfo() {
        final ClientAPI clientAPI = App.consultancyRetrofit().create(ClientAPI.class);
        clientAPI.getClientDetail(App.db().getInt(Keys.CLIENT_ID)).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().data.client.detail != null) {
                            String imageUrl = response.body().data.client.detail.cover_photo;
                            String logoUrl = response.body().data.client.logo;

                            Picasso.get().load(imageUrl).into(banner);
                            Picasso.get().load(logoUrl).into(logo);
                            name.setText(response.body().data.client.client_name);
                            email.setText(response.body().data.client.detail.location);
                        } else {
                            Picasso.get().load(R.drawable.banner).into(banner);
                        }
                    }

                } else {
                    try {
                        Log.d("client", "onResponse: error" + response.errorBody().string());
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                MDToast mdToast = MDToast.makeText(getActivity(), "There is no internet connection. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                mdToast.show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ConsultancyProfileViewPagerAdapter adapter = new ConsultancyProfileViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new ProfieAboutFragment(), "About");
        switch (BuildConfig.FLAVOR) {
            case Client.RATIONAL:
                adapter.addFragment(new UniversityFragment(), "University");
                break;

            default:
                break;
        }
        adapter.addFragment(new GalleryFragment(), "Gallery");
        viewPager.setAdapter(adapter);
    }

}
