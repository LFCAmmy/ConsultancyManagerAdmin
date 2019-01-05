package susankyatech.com.consultancymanageradmin.FileTransfer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.security.Key;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import susankyatech.com.consultancymanageradmin.API.StudentAPI;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Model.Login;
import susankyatech.com.consultancymanageradmin.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserVerificationFragment extends Fragment {

    @BindView(R.id.student_code)
    EditText studentCode;
    @BindView(R.id.year)
    EditText year;
    @BindView(R.id.month)
    EditText month;
    @BindView(R.id.day)
    EditText day;
    @BindView(R.id.btn_verify_student)
    FancyButton verify;

    public UserVerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_verification, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Verify User");
        init();
        return view;
    }

    private void init() {
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = studentCode.getText().toString();
                String yrs = year.getText().toString();
                String mth = month.getText().toString();
                String days = day.getText().toString();

                if (TextUtils.isEmpty(code)){
                    studentCode.setError("Enter Student Code");
                    studentCode.requestFocus();
                } else if (TextUtils.isEmpty(yrs)){
                    year.setError("Enter year");
                    year.requestFocus();
                } else if (TextUtils.isEmpty(mth)){
                    month.setError("Enter month");
                    month.requestFocus();
                } else if (TextUtils.isEmpty(yrs)){
                    day.setError("Enter day");
                    day.requestFocus();
                } else {
                    String dob = yrs + "-" + mth + "-" + days;
                    verifyStudent(code, dob);
                }
            }
        });
    }

    private void verifyStudent(String code, String dob) {
        StudentAPI studentAPI = App.consultancyRetrofit().create(StudentAPI.class);
        studentAPI.verifyStudent(dob, code).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().success){
                            App.hideKeyboard(getActivity());
                            App.db().putBoolean(Keys.IS_VERIFIED, true);
                            FragmentTransaction fragmentTransaction = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                           fragmentTransaction.replace(R.id.main_container, new FileTransferFragment()).addToBackStack(null).commit();
                        }else {
                            studentCode.setText("");
                            year.setText("");
                            month.setText("");
                            day.setText("");
                            MDToast mdToast = MDToast.makeText(getContext(), "Student code or DOB incorrect, Please enter again", Toast.LENGTH_LONG, MDToast.TYPE_WARNING);
                            mdToast.show();
                        }
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

}
