package susankyatech.com.consultancymanageradmin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Model.Notice;
import susankyatech.com.consultancymanageradmin.Notice.NoticeFragment;
import susankyatech.com.consultancymanageradmin.R;

import static java.security.AccessController.getContext;

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.NoticeListViewHolder> {

    private List<Notice> noticeList;
    private Context context;

    public NoticeListAdapter(List<Notice> noticeList, Context context) {
        this.noticeList = noticeList;
        this.context = context;
    }

    @NonNull
    @Override
    public NoticeListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_notice_layout, viewGroup, false);
        return new NoticeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoticeListViewHolder holder, final int position) {
       holder.categoryInitial.setText(noticeList.get(position).notice_category.substring(0,1));
        holder.category.setText(noticeList.get(position).notice_category);
        holder.title.setText(noticeList.get(position).title);
        holder.desc.setText(noticeList.get(position).description);


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(noticeList.get(position).created_at);
            String createdDate = dateFormat.format(date);
            holder.date.setText(createdDate);
        } catch (ParseException e){

        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", noticeList.get(position).title);
                bundle.putString("category", noticeList.get(position).notice_category);
                bundle.putString("notice", noticeList.get(position).description);
                bundle.putString("date", noticeList.get(position).created_at);

                FragmentTransaction fragmentTransaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                NoticeFragment noticeFragment = new NoticeFragment();
                noticeFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.main_container, noticeFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class NoticeListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notice_layout)
        View layout;
        @BindView(R.id.notice_category_initial)
        TextView categoryInitial;
        @BindView(R.id.notice_category)
        TextView category;
        @BindView(R.id.notice_title)
        TextView title;
        @BindView(R.id.notice_description)
        TextView desc;
        @BindView(R.id.notice_date)
        TextView date;

        public NoticeListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
