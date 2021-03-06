package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import susankyatech.com.consultancymanageradmin.API.EnquiryAPI;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Model.CountryList;
import susankyatech.com.consultancymanageradmin.Model.Course;
import susankyatech.com.consultancymanageradmin.Model.Login;
import susankyatech.com.consultancymanageradmin.R;


import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenInquirySelectCountryFragment extends Fragment {

    @BindView(R.id.country_name)
    AutoCompleteTextView countryName;
    @BindView(R.id.course_name)
    AutoCompleteTextView courseName;
    @BindView(R.id.btn_next)
    FancyButton btnNext;

    private boolean isConsultancy;

    CountryList countryList= new CountryList();
    private List<String> courseList = new ArrayList<>();


    public OpenInquirySelectCountryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open_inquiry_select_country, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Send Inquiry");
        init();

        return view;
    }

    private void init() {
        if (getArguments() != null){
            isConsultancy = getArguments().getBoolean(FragmentKeys.ISCONSULTANCY);
        }

        countryName.requestFocus();

        getCourseList();

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.select_dialog_item, countryList.countyList);

        countryName.setThreshold(1);
        countryName.setAdapter(countryAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = countryName.getText().toString();
                String courses = courseName.getText().toString();
                if (TextUtils.isEmpty(country)){
                    countryName.setError("Enter Country Name");
                    countryName.requestFocus();
                } else if (TextUtils.isEmpty(courses)){
                    courseName.setError("Enter Courses Name");
                    courseName.requestFocus();
                }else {
                    EnquiryAPI enquiryAPI = App.consultancyRetrofit().create(EnquiryAPI.class);
                    enquiryAPI.saveCountryAndCourse(country, courses, App.db().getInt(Keys.USER_ID)).enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if (response.isSuccessful()){
                                if (response.body() != null){
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(FragmentKeys.ISCONSULTANCY, isConsultancy);
                                    App.db().putObject(FragmentKeys.DATA, response.body().data);

                                    App.hideKeyboard(getActivity());

                                    FragmentTransaction fragmentTransaction = ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction();
                                    OpenInquiryProfileFragment openInquirySelectCountryFragment = new OpenInquiryProfileFragment();
                                    openInquirySelectCountryFragment.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.main_container, openInquirySelectCountryFragment).addToBackStack(null).commit();
                                }
                            }else {
                                try {
                                    Log.d("nbv", "onResponse: error" + response.errorBody().string());
                                    MDToast mdToast = MDToast.makeText(getContext(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                                    mdToast.show();
                                } catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Log.d(TAG, "nbv: "+t.getMessage());
                            MDToast mdToast = MDToast.makeText(getActivity(), "There is no internet connection. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                            mdToast.show();
                        }
                    });

                }
            }
        });


    }

    private void getCourseList() {
        EnquiryAPI courseAPI = App.consultancyRetrofit().create(EnquiryAPI.class);
        courseAPI.getStudentCourses().enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        courseList = response.body().data;
                        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>
                                (getContext(), android.R.layout.select_dialog_item, courseList);
                        courseName.setThreshold(1);
                        courseName.setAdapter(courseAdapter);
                        Log.d("mnb", "onResponse: "+courseList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {

            }
        });
    }

}
