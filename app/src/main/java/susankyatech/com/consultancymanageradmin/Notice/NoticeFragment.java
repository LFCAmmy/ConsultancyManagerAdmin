package susankyatech.com.consultancymanageradmin.Notice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment {

    @BindView(R.id.notice_title)
    TextView noticeTitle;
    @BindView(R.id.notice_category_initial)
    TextView categoryInitial;
    @BindView(R.id.notice_category)
    TextView noticeCategory;
    @BindView(R.id.notice_date)
    TextView noticeDate;
    @BindView(R.id.notice_description)
    TextView noticeDesc;

    String title, category, notice, date;

    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (getArguments() != null){
            title = getArguments().getString("title");
            category = getArguments().getString("category");
            notice = getArguments().getString("notice");
            date = getArguments().getString("date");
        }

        noticeTitle.setText(title);
        noticeCategory.setText(category);
        noticeDesc.setText(notice);

        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date d = simpleDateFormat.parse(date);
            String createdDate = simpleDateFormat.format(d);
            noticeDate.setText(createdDate);
        } catch (ParseException e){}
        categoryInitial.setText(category.substring(0,1));
    }

}
