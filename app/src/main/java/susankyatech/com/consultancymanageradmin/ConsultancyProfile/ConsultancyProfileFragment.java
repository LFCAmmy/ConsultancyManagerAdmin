package susankyatech.com.consultancymanageradmin.ConsultancyProfile;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import susankyatech.com.consultancymanageradmin.Adapter.ConsultancyProfileViewPagerAdapter;
import susankyatech.com.consultancymanageradmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultancyProfileFragment extends Fragment {

    @BindView(R.id.profile_tabs)
    TabLayout tabLayout;
    @BindView(R.id.profile_viewpager)
    ViewPager viewPager;
    @BindView(R.id.profile_banner)
    ImageView banner;
    @BindView(R.id.consultancy_logo)
    CircleImageView logo;
    @BindView(R.id.consultancy_name)
    TextView name;
    @BindView(R.id.consultancy_email)
    TextView email;

    public ConsultancyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultancy_profile, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setupViewPager(ViewPager viewPager) {
        ConsultancyProfileViewPagerAdapter adapter = new ConsultancyProfileViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new ProfieAboutFragment(), "About");
        adapter.addFragment(new GalleryFragment(), "Gallery");
        viewPager.setAdapter(adapter);
    }

}
