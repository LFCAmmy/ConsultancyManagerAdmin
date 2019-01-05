package susankyatech.com.consultancymanageradmin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.ConsultancyProfile.UniversityFragment;
import susankyatech.com.consultancymanageradmin.ConsultancyProfile.UniversityProfileFragment;
import susankyatech.com.consultancymanageradmin.Model.Courses;
import susankyatech.com.consultancymanageradmin.R;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    private Context context;
    private List<Courses> courseList;
    private List<Courses> arrayList;

    private Boolean clicked = false;

    public CourseListAdapter(Context context, List<Courses> courseList) {
        this.context = context;
        this.courseList = courseList;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(UniversityProfileFragment.courseList);
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_course_layout, viewGroup, false);
        return new CourseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseListViewHolder holder, int i) {

        holder.courseName.setText(courseList.get(i).name);

        holder.courseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clicked){
                    clicked = false;
                    holder.arrow.setImageResource(R.drawable.ic_arrow_down);
                    holder.detailLayout.setVisibility(View.GONE);
                } else {
                    clicked = true;
                    holder.arrow.setImageResource(R.drawable.ic_up_arrow);
                    holder.detailLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        holder.courseLevel.setText(courseList.get(i).description);
        holder.courseFee.setText(courseList.get(i).fees);
        holder.courseIntake.setText(courseList.get(i).intakes);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        UniversityProfileFragment.courseList.clear();
        if (charText.length() == 0) {
            UniversityProfileFragment.courseList.addAll(arrayList);
        } else {
            for (Courses wp : arrayList) {
                if (wp.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    UniversityProfileFragment.courseList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.course_name)
        TextView courseName;

        @BindView(R.id.courseLayout)
        View courseLayout;

        @BindView(R.id.courseDetail)
        View detailLayout;

        @BindView(R.id.course_level)
        TextView courseLevel;

        @BindView(R.id.course_fee)
        TextView courseFee;

        @BindView(R.id.course_intake)
        TextView courseIntake;

        @BindView(R.id.list_children_course_arrow)
        ImageView arrow;

        private CourseListViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
