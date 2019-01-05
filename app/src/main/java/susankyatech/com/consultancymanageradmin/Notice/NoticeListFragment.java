package susankyatech.com.consultancymanageradmin.Notice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import susankyatech.com.consultancymanageradmin.API.NoticeAPI;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Adapter.NoticeListAdapter;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Decorations.GridViewItemDecoration;
import susankyatech.com.consultancymanageradmin.Model.Notice;
import susankyatech.com.consultancymanageradmin.Model.NoticeResponse;
import susankyatech.com.consultancymanageradmin.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeListFragment extends Fragment {

    @BindView(R.id.notice_list)
    RecyclerView recyclerView;
    @BindView(R.id.message)
    TextView message;

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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Notice");
        return view;
    }

    private void init() {
        getNoticeList();

    }

    private void getNoticeList() {
        NoticeAPI noticeAPI = App.consultancyRetrofit().create(NoticeAPI.class);
        noticeAPI.getNotices().enqueue(new Callback<NoticeResponse>() {
            @Override
            public void onResponse(Call<NoticeResponse> call, Response<NoticeResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    noticeList = new ArrayList<>();
                    noticeList = response.body().data;
                    if (noticeList.size() == 0){
                        message.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        adapter = new NoticeListAdapter(noticeList, getContext());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        recyclerView.addItemDecoration(new GridViewItemDecoration(getContext()));
                    }
                }else {
                    try {
                        Log.d("client", "onResponse: error" + response.errorBody().string());
                        MDToast mdToast = MDToast.makeText(getActivity(), "There is problem retrieving notice detail. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                        mdToast.show();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<NoticeResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                MDToast mdToast = MDToast.makeText(getActivity(), "There is no internet connection. Please try again later!", Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                mdToast.show();
            }
        });



    }

}
