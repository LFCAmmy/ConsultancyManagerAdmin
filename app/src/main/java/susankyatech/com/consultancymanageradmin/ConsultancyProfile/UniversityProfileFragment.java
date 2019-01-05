package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.Adapter.CourseListAdapter;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Decorations.VerticalSpaceItemDecoration;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Model.Courses;
import susankyatech.com.consultancymanageradmin.Model.University;
import susankyatech.com.consultancymanageradmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UniversityProfileFragment extends Fragment {

    @BindView(R.id.university_name)
    TextView universityName;
    @BindView(R.id.university_email)
    TextView universityEmail;
    @BindView(R.id.university_location)
    TextView universityLocation;
    @BindView(R.id.university_description)
    TextView universityDesc;
    @BindView(R.id.university_description_layout)
    View descriptionLayout;
    @BindView(R.id.courseList)
    RecyclerView recyclerView;
    @BindView(R.id.progressBarLayout)
    View progressLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressTV)
    TextView progressTextView;
    @BindView(R.id.whole_layout)
    View wholeLayout;
    @BindView(R.id.searchCourse)
    SearchView courseSearch;


    private University university;

    private CourseListAdapter adapter;

    public static List<Courses> courseList;

    public UniversityProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_university_profile, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        getCourse();

        courseSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                String text = s;
                adapter.filter(text);
                recyclerView.setAdapter(adapter);
                return false;

            }
        });

        adapter = new CourseListAdapter(getContext(), courseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(12));

    }

    private void getCourse() {
        university = App.db().getObject(FragmentKeys.UNIVERSITY, University.class);

        universityName.setText(university.name);
        universityEmail.setText(university.email);
        universityLocation.setText(university.address);

        if (university.description != null) {
            universityDesc.setText(university.description);
        } else {
            descriptionLayout.setVisibility(View.GONE);
        }

        courseList = new ArrayList<>();
        courseList = university.course;
    }

}
