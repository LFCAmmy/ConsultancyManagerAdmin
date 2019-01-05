package susankyatech.com.consultancymanageradmin.Profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Model.Data;
import susankyatech.com.consultancymanageradmin.Model.StudentDetail;
import susankyatech.com.consultancymanageradmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {

    @BindView(R.id.profile_name)
    TextView fullName;
    @BindView(R.id.student_id)
    TextView studentId;
    @BindView(R.id.admission_date)
    TextView admissionDate;
    @BindView(R.id.student_phone)
    TextView phone;
    @BindView(R.id.student_email)
    TextView email;
    @BindView(R.id.student_location)
    TextView location;
    @BindView(R.id.student_dob)
    TextView dob;
    @BindView(R.id.student_gender)
    TextView gender;
    @BindView(R.id.student_qualification)
    TextView qualification;
    @BindView(R.id.father_name)
    TextView fatherName;
    @BindView(R.id.mother_name)
    TextView motherName;
    @BindView(R.id.interested_country)
    TextView destination;
    @BindView(R.id.interested_course)
    TextView interestedCourse;
    @BindView(R.id.secondary_phone)
    TextView secondaryPhone;
    @BindView(R.id.secondary_address)
    TextView secondaryAddress;
    @BindView(R.id.reference)
    TextView reference;
    @BindView(R.id.edit_info)
    ImageView edit;
    @BindView(R.id.profile_pic)
    CircleImageView profilePic;

    private Data data;
    private StudentDetail detail;


    public StudentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        ((MainActivity)getActivity()).getSupportActionBar().show();
        init();
        return view;
    }

    private void init() {
        data = App.db().getObject(FragmentKeys.DATA, Data.class);
        detail = data.detail;

        fullName.setText(data.name);
        admissionDate.setText(data.created_at);
        phone.setText(data.phone);
        email.setText(data.email);
        location.setText(data.address);
        dob.setText(data.dob);
        studentId.setText(data.code);
        gender.setText(data.gender);
        fatherName.setText(detail.father_name);
        motherName.setText(detail.mother_name);
        secondaryAddress.setText(detail.secondary_address);
        secondaryPhone.setText(detail.secondary_phone);
        destination.setText(detail.interested_country);
        interestedCourse.setText(detail.interested_course);
        reference.setText(data.reference);

        Picasso.get().load(data.profile_image).placeholder(R.drawable.user).into(profilePic);

        qualification.setText(detail.qualification.get(0) + ", " + detail.qualification.get(1));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(FragmentKeys.FRAGMENTNAME, "profile");

                FragmentTransaction fragmentTransaction = ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction();
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                editProfileFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.main_container, editProfileFragment).addToBackStack(null).commit();

            }
        });

    }

}
