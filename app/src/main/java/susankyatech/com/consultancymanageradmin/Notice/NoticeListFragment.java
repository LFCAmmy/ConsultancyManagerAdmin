package susankyatech.com.consultancymanageradmin.Notice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.Adapter.NoticeListAdapter;
import susankyatech.com.consultancymanageradmin.Decorations.GridViewItemDecoration;
import susankyatech.com.consultancymanageradmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeListFragment extends Fragment {

    @BindView(R.id.notice_list)
    RecyclerView recyclerView;

    private List<Notice> noticeList;

    private NoticeListAdapter adapter;

    public NoticeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        getNoticeList();

    }

    private void getNoticeList() {
        noticeList = new ArrayList<>();
        noticeList.add(new Notice("Title1", "General", "25 Nov", "k cha halkhabar..."));
        noticeList.add(new Notice("Title2", "Holiday", "1 Dec", "hi..."));
        noticeList.add(new Notice("Title3", "General", "18 Jan", "bye..."));
        noticeList.add(new Notice("Title4", "General", "20 Oct", "k cha?..."));

        adapter = new NoticeListAdapter(noticeList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridViewItemDecoration(getContext()));

    }

}
