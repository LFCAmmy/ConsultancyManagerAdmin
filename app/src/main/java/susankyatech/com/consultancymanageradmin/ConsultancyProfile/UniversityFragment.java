package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import susankyatech.com.consultancymanageradmin.API.ClientAPI;
import susankyatech.com.consultancymanageradmin.Adapter.UniversityListAdapter;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Decorations.VerticalSpaceItemDecoration;
import susankyatech.com.consultancymanageradmin.Model.University;
import susankyatech.com.consultancymanageradmin.Model.UniversityResponse;
import susankyatech.com.consultancymanageradmin.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class UniversityFragment extends Fragment {

    @BindView(R.id.university_rv)
    RecyclerView recyclerView;
    @BindView(R.id.progressBarLayout)
    View progressLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressTV)
    TextView progressTextView;

    private List<University> universities = new ArrayList<>();
    private UniversityListAdapter adapter;

    public UniversityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_university, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getUniversity();
    }

    private void getUniversity() {
        ClientAPI clientAPI = App.rationalRetrofit().create(ClientAPI.class);
        clientAPI.getUniversity().enqueue(new Callback<UniversityResponse>() {
            @Override
            public void onResponse(Call<UniversityResponse> call, Response<UniversityResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    progressLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    universities = response.body().data;
                    adapter = new UniversityListAdapter(universities, getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(15));
                }else {
                    try {
                        Log.d("client", "onResponse: error" + response.errorBody().string());
                        MDToast mdToast = MDToast.makeText(getActivity(), "Problem occured while retrieving University List!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                        mdToast.show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<UniversityResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                MDToast mdToast = MDToast.makeText(getActivity(), "There is no internet connection. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                mdToast.show();
            }
        });
    }

}
