package susankyatech.com.consultancymanageradmin.Profile;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import susankyatech.com.consultancymanageradmin.API.StudentAPI;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.ConsultancyProfile.OpenInquiryProfileFragment;
import susankyatech.com.consultancymanageradmin.Generic.FileUtils;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Model.Data;
import susankyatech.com.consultancymanageradmin.Model.Login;
import susankyatech.com.consultancymanageradmin.Model.StudentDetail;
import susankyatech.com.consultancymanageradmin.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    @BindView(R.id.setting_profile_pic)
    CircleImageView profilePic;
    @BindView(R.id.setting_pro_pic_edit)
    TextView editProfilePic;
    @BindView(R.id.personal_info_fullname)
    EditText fullName;
    @BindView(R.id.year)
    EditText year;
    @BindView(R.id.month)
    EditText month;
    @BindView(R.id.day)
    EditText day;
    @BindView(R.id.personal_info_gender)
    Spinner gender;
    @BindView(R.id.personal_info_qualification)
    EditText qualification;
    @BindView(R.id.personal_info_father)
    EditText fatherName;
    @BindView(R.id.personal_info_mother)
    EditText motherName;
    @BindView(R.id.personal_info_phone)
    EditText phone;
    @BindView(R.id.personal_info_email)
    EditText email;
    @BindView(R.id.personal_info_address)
    EditText address;
    @BindView(R.id.personal_info_destination)
    EditText destination;
    @BindView(R.id.personal_info_interested_course)
    EditText interestedCourse;
    @BindView(R.id.personal_info_secondary_phone)
    EditText secondaryPhone;
    @BindView(R.id.personal_info_secondary_address)
    EditText secondaryAddress;
    @BindView(R.id.setting_save)
    TextView save;
    @BindView(R.id.setting_cancel)
    TextView cancel;

    private Data data;
    private StudentDetail detail;
    private File file;

    private String[] sex = {"Male", "Female"};
    private String selectedUserGender, fragmentName;
    private Boolean picUploaded = false, isConsultancy;

    final static int RESULT_LOAD_IMAGE = 1;
    private Uri imageUri;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Edit Profile");
        ((MainActivity)getActivity()).getSupportActionBar().hide();
        init();
        return view;
    }

    private void init() {
        if (getArguments() != null){
            fragmentName = getArguments().getString(FragmentKeys.FRAGMENTNAME);
            isConsultancy = getArguments().getBoolean(FragmentKeys.ISCONSULTANCY);
        }
        data = App.db().getObject(FragmentKeys.DATA, Data.class);
        detail = data.detail;

        fullName.setText(data.name);
        qualification.setText(detail.qualification.get(0) + "," + detail.qualification.get(1));
        fatherName.setText(detail.father_name);
        motherName.setText(detail.mother_name);
        phone.setText(data.phone);
        email.setText(data.email);
        address.setText(data.address);
        destination.setText(detail.interested_country);
        interestedCourse.setText(detail.interested_course);
        secondaryPhone.setText(detail.secondary_phone);
        secondaryAddress.setText(detail.secondary_address);

        Picasso.get().load(data.profile_image).placeholder(R.drawable.user).into(profilePic);

        String[] newDOb = data.dob.split("-");
        year.setText(newDOb[0]);
        month.setText(newDOb[1]);
        day.setText(newDOb[2]);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sex);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        for (int i = 0; i < sex.length; i++){
            if (sex[i].equals(gender)){
                gender.setSelection(i);
            }else {
                gender.setSelection(0);
            }
        }

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUserGender = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentName.equals("enquiry")){
                    sendUserToInquiry();
                } else {
                    sendUserToProfile();
                }
            }
        });

        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
                    } else {
                        openFilePicker();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = fullName.getText().toString();
                String userPhone = phone.getText().toString();
                String userEmail = email.getText().toString();
                String userAddress = address.getText().toString();
                String dobYear = year.getText().toString();
                String dobMth = month.getText().toString();
                String dobDay = day.getText().toString();
                final String userQualification = qualification.getText().toString();
                final String userFather = fatherName.getText().toString();
                final String userMother = motherName.getText().toString();
                final String userDestination = destination.getText().toString();
                final String userCourse = interestedCourse.getText().toString();
                final String userSecPhone = secondaryPhone.getText().toString();
                final String userSecAddress = secondaryAddress.getText().toString();

                if (TextUtils.isEmpty(userName)){
                    fullName.setError("Enter your name");
                    fullName.requestFocus();
                } else if (TextUtils.isEmpty(userPhone)){
                    phone.setError("Enter your phone");
                    phone.requestFocus();
                } else if (TextUtils.isEmpty(userEmail)){
                    email.setError("Enter your email");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(userAddress)){
                    address.setError("Enter your address");
                    address.requestFocus();
                } else if (TextUtils.isEmpty(dobYear)){
                    year.setError("Enter your birth year");
                    year.requestFocus();
                } else if (TextUtils.isEmpty(dobMth)){
                    month.setError("Enter your birth month");
                    month.requestFocus();
                } else if (TextUtils.isEmpty(dobDay)){
                    day.setError("Enter your birth day");
                    day.requestFocus();
                } else {
                    String dob = dobYear + "-" + dobMth + "-" + dobDay;
                    StudentAPI studentAPI = App.consultancyRetrofit().create(StudentAPI.class);
                    studentAPI.changePrimaryInfo(userEmail, userName, userAddress, userPhone, dob , selectedUserGender)
                            .enqueue(new Callback<Login>() {
                                @Override
                                public void onResponse(Call<Login> call, Response<Login> response) {
                                    if (response.isSuccessful()){
                                        if (response.body() != null){
                                            if (picUploaded){
                                                uploadUserImage(userQualification, userFather, userMother, userDestination, userCourse, userSecPhone, userSecAddress);
                                            }else {
                                                editFurtherDetail(userQualification, userFather, userMother, userDestination, userCourse, userSecPhone, userSecAddress);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Login> call, Throwable t) {

                                }
                            });
                }

            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    private void uploadUserImage(final String userQualification, final String userFather, final String userMother, final String userDestination, final String userCourse, final String userSecPhone, final String userSecAddress) {
        RequestBody fileBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_image", file.getName(), fileBody);

        String studentId = String.valueOf(App.db().getInt(Keys.USER_ID));
        RequestBody fileId = RequestBody.create(MediaType.parse("text/plain"), studentId);

        StudentAPI studentAPI = App.consultancyRetrofit().create(StudentAPI.class);
        studentAPI.addProfilePic(fileId, fileToUpload).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    editFurtherDetail(userQualification, userFather, userMother, userDestination, userCourse, userSecPhone, userSecAddress);
                }else {
                    try {
                        Log.d("client", "onResponse: error" + response.errorBody().string());
                        MDToast mdToast = MDToast.makeText(getContext(), "There was something wrong while saving your profile pic. Please try again!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                        mdToast.show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                MDToast mdToast = MDToast.makeText(getActivity(), "There is no internet connection. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                mdToast.show();
            }
        });

    }

    private void sendUserToProfile(){
        App.hideKeyboard(getActivity());
        FragmentTransaction fragmentTransaction = ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction();
        StudentProfileFragment studentProfileFragment = new StudentProfileFragment();
        fragmentTransaction.replace(R.id.main_container, studentProfileFragment).addToBackStack(null).commit();
    }

    private void sendUserToInquiry(){
        App.hideKeyboard(getActivity());
        Bundle bundle = new Bundle();
        bundle.putBoolean(FragmentKeys.ISCONSULTANCY, isConsultancy);

        FragmentTransaction fragmentTransaction = ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction();
        OpenInquiryProfileFragment openInquirySelectCountryFragment = new OpenInquiryProfileFragment();
        openInquirySelectCountryFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, openInquirySelectCountryFragment).addToBackStack(null).commit();
    }

    private void editFurtherDetail(String userQualification, String userFather, String userMother, String userDestination, String userCourse, String userSecPhone, String userSecAddress) {
        StudentAPI studentAPI = App.consultancyRetrofit().create(StudentAPI.class);
        studentAPI.saveDetail(userQualification, userFather, userMother, App.db().getInt(Keys.USER_ID),userDestination, userCourse, userSecPhone, userSecAddress)
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null){
                                App.db().putObject(FragmentKeys.DATA, response.body().data);
                                MDToast mdToast = MDToast.makeText(getContext(), "Your info is successfully saved!", Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                                mdToast.show();
                                sendUserToProfile();
                            }
                        }else {
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
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        MDToast mdToast = MDToast.makeText(getActivity(), "There is no internet connection. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                        mdToast.show();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == RESULT_LOAD_IMAGE) && (resultCode == -1)) {
            String fileName = FileUtils.getPath(getContext(),data.getData());
            file = new File(fileName);

            imageUri = data.getData();

            profilePic.setImageURI(imageUri);
            picUploaded = true;
        }
    }





}
