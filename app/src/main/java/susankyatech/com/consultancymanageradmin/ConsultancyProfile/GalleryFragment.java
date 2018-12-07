package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Adapter.GalleryListAdapter;
import susankyatech.com.consultancymanageradmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    @BindView(R.id.gallery_list)
    RecyclerView galleryList;
    @BindView(R.id.message)
    TextView cardView;

    private GalleryListAdapter adapter;
    List<Gallery> allGallery;
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
        init();
        return view;
    }

    private void init() {
        listGallery();

    }

    private void listGallery() {
        allGallery = new ArrayList<>();
        allGallery.add(new Gallery("https://pixel.nymag.com/imgs/daily/vulture/2016/05/31/31-amber-heard.w330.h330.jpg"));
        allGallery.add(new Gallery("https://media.vanityfair.com/photos/5772d8cd38c86e69346ea748/master/w_768,c_limit/ss04-margot-robbie-august-2016.jpg"));
        allGallery.add(new Gallery("https://hips.hearstapps.com/hbz.h-cdn.co/assets/16/36/1473631592-hbz-gigi-hadid-october-2016-08.jpg"));
        allGallery.add(new Gallery("https://akns-images.eonline.com/eol_images/Entire_Site/2015313/rs_634x865-150413082646-634.fashion-magazine-may-2015-elizabeth-olsen-02.jpg"));

        images = new ArrayList<>();
        for(int i=0;i<allGallery.size();i++)
            images.add(allGallery.get(i).image);

        adapter = new GalleryListAdapter(allGallery, images, getContext());
        galleryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        galleryList.setAdapter(adapter);

    }

}
