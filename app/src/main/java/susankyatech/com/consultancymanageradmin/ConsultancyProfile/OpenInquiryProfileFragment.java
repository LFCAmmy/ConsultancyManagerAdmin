package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Model.Data;
import susankyatech.com.consultancymanageradmin.Model.Detail;
import susankyatech.com.consultancymanageradmin.Model.StudentDetail;
import susankyatech.com.consultancymanageradmin.Profile.EditProfileFragment;
import susankyatech.com.consultancymanageradmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenInquiryProfileFragment extends Fragment {

    @BindView(R.id.qualification)
    TextView qualificationTv;
    @BindView(R.id.complete_year)
    TextView completeYearTv;
    @BindView(R.id.address)
    TextView addressTv;
    @BindView(R.id.contact)
    TextView contactTv;
    @BindView(R.id.name)
    TextView nameTv;
    @BindView(R.id.email)
    TextView emailIdTv;
    @BindView(R.id.dob)
    TextView dobTv;
    @BindView(R.id.btn_edit)
    FancyButton btnEdit;
    @BindView(R.id.btn_back)
    FancyButton btnBack;
    @BindView(R.id.btn_send)
    FancyButton btnSend;

    private boolean isConsultancy;

    private Data data;
    private StudentDetail detail;

    List<Integer> dates = new ArrayList<>();

    public OpenInquiryProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open_inquiry_profile, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Send Inquiry");
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (getArguments() != null) {
            isConsultancy = getArguments().getBoolean(FragmentKeys.ISCONSULTANCY);
        }

        int todayYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = todayYear; i > 1969; i--) {
            dates.add(i);
        }

        getStudentInfo();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(FragmentKeys.ISCONSULTANCY, isConsultancy);

                FragmentTransaction fragmentTransaction = ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction();
                OpenInquirySelectCountryFragment openInquirySelectCountryFragment = new OpenInquirySelectCountryFragment();
                openInquirySelectCountryFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.main_container, openInquirySelectCountryFragment).addToBackStack(null).commit();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editStudentDetails();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConsultancy) {
                    sendInquiryToConsultancy();
                } else {
                    sendInquiryToUniversity();
                }

            }
        });
    }

    private void sendInquiryToUniversity() {
        Toast.makeText(getContext(), "University", Toast.LENGTH_SHORT).show();

    }

    private void sendInquiryToConsultancy() {
        Toast.makeText(getContext(), "Consultancy", Toast.LENGTH_SHORT).show();
    }

    private void editStudentDetails() {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentKeys.FRAGMENTNAME, "enquiry");
        bundle.putBoolean(FragmentKeys.ISCONSULTANCY, isConsultancy);

        FragmentTransaction fragmentTransaction = ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction();
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        editProfileFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, editProfileFragment).addToBackStack(null).commit();

    }

    private void getStudentInfo() {
        data = App.db().getObject(FragmentKeys.DATA,Data.class);
        detail = data.detail;
        qualificationTv.setText(detail.qualification.get(0) + "," + detail.qualification.get(1));
        nameTv.setText(data.name);
        addressTv.setText(data.address);
        contactTv.setText(data.phone);
        emailIdTv.setText(data.email);
        if (data.dob != null) {
            dobTv.setText(data.dob);
        }
        completeYearTv.setText(detail.completed_year);
    }

}
