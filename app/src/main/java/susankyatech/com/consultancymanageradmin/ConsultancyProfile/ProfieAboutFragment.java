package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.BuildConfig;
import susankyatech.com.consultancymanageradmin.Generic.Client;
import susankyatech.com.consultancymanageradmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfieAboutFragment extends Fragment {

    @BindView(R.id.read_more)
    TextView readMore;



    public ProfieAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int resourceToLoad;

        switch (BuildConfig.FLAVOR)
        {
            case Client
                    .ACHIEVERS:
                resourceToLoad=R.layout.achievers_layout_profile;
            break;

            case Client.RATIONAL:
                resourceToLoad=R.layout.rational_layout_profile;
                break;

            default:
                resourceToLoad=R.layout.fragment_profie_about;
        }
        // Inflate the layout for this fragment
        View v= inflater.inflate(resourceToLoad, container, false);
        ButterKnife.bind(this, v);
        init();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");
        return v;
    }

    private void init() {
        readMore.setVisibility(View.GONE);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://susankya.com.np/public/external_projects/rational/"));
                startActivity(browserIntent);
            }
        });
    }

}
