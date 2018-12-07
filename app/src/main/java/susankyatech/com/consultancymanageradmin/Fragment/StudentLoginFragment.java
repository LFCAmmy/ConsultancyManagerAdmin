package susankyatech.com.consultancymanageradmin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentLoginFragment extends Fragment {

    @BindView(R.id.login_btn)
    FancyButton loginBtn;

    public StudentLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_login, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
    }

}
