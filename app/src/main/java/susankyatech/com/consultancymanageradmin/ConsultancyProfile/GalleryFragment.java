package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Adapter.GalleryListAdapter;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Model.Login;
import susankyatech.com.consultancymanageradmin.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    @BindView(R.id.gallery_list)
    RecyclerView galleryList;
    @BindView(R.id.message)
    TextView cardView;
    @BindView(R.id.progressBarLayout)
    View progressLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressTV)
    TextView progressTextView;

    private GalleryListAdapter adapter;
    List<Gallery> allGallery = new ArrayList<>();
    ArrayList<String> images;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().show();
        ButterKnife.bind(this,view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");
        init();
        return view;
    }

    private void init() {
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        galleryList.setVisibility(View.GONE);
        listGallery();
        galleryList.setLayoutManager(new GridLayoutManager(getContext(),2));

    }

    private void listGallery() {
        final ClientAPI clientAPI = App.consultancyRetrofit().create(ClientAPI.class);
        clientAPI.getClientDetail(App.db().getInt(Keys.CLIENT_ID)).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        progressLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        galleryList.setVisibility(View.VISIBLE);

                        allGallery = response.body().data.client.galleries;
                        if (allGallery.size() == 0){
                            cardView.setVisibility(View.VISIBLE);
                            cardView.setText("No Gallery Found");
                        }else{
                            cardView.setVisibility(View.GONE);
                        }
                        images = new ArrayList<>();
                        for(int i=0;i<allGallery.size();i++)
                            images.add(allGallery.get(i).image);

                        Log.d("asd", "onResponse: "+allGallery.size());
                        adapter = new GalleryListAdapter(allGallery, images, getContext());
                        galleryList.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                MDToast mdToast = MDToast.makeText(getActivity(), "There is no internet connection. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                mdToast.show();
            }
        });

    }

}
