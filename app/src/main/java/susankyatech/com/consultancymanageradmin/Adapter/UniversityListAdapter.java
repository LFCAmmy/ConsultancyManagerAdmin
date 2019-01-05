package susankyatech.com.consultancymanageradmin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.ConsultancyProfile.UniversityProfileFragment;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Model.University;
import susankyatech.com.consultancymanageradmin.R;

public class UniversityListAdapter extends RecyclerView.Adapter<UniversityListAdapter.UniversityViewHolder> {

    private List<University> universityList;
    private Context context;

    public UniversityListAdapter(List<University> universityList, Context context) {
        this.universityList = universityList;
        this.context = context;
    }

    @NonNull
    @Override
    public UniversityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_university_layout, viewGroup, false);
        return new UniversityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityViewHolder holder, final int i) {
        holder.name.setText(universityList.get(i).name);
        holder.address.setText(universityList.get(i).address + ", " + universityList.get(i).country_name);
        holder.details.setText(universityList.get(i).email);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.db().putObject(FragmentKeys.UNIVERSITY, universityList.get(i));
                FragmentTransaction fragmentTransaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, new UniversityProfileFragment()).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return universityList.size();
    }

    public class UniversityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.university_logo)
        ImageView logo;
        @BindView(R.id.university_name)
        TextView name;
        @BindView(R.id.university_address)
        TextView address;
        @BindView(R.id.university_details)
        TextView details;
        @BindView(R.id.main_layout)
        View mainView;

        public UniversityViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
