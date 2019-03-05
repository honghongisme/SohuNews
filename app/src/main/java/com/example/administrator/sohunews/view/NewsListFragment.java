package com.example.administrator.sohunews.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.sohunews.R;
import com.example.administrator.sohunews.adapter.NewsListAdapter;
import com.example.administrator.sohunews.contract.INewsListContract;
import com.example.administrator.sohunews.model.bean.PictureNews;
import com.example.administrator.sohunews.presenter.NewsListPresenter;

import java.util.ArrayList;


public class NewsListFragment extends Fragment implements INewsListContract.View {

    private static final int REQUEST_NEWS_ITEM_SUCCESS = 0;
    private static final int REQUEST_NEWS_ITEM_FAILED = 1;

    @BindView(R.id.rv)
    RecyclerView mRv;

    private View mLoadingLayout;
    private ImageView loadingIv;
    private View mLayout;
    private String mUrl;
    private ArrayList<PictureNews> mData;
    private NewsListPresenter mPresenter;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_NEWS_ITEM_SUCCESS:
                    stopLoadingAnim();
                    refreshRecycleView((ArrayList<PictureNews>) msg.obj);
                    break;
                case REQUEST_NEWS_ITEM_FAILED:
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the mLayout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_new, container, false);
        ButterKnife.bind(this, mLayout);
        init();

        return mLayout;
    }

    public void init() {
        mData = new ArrayList<>();
        mUrl = getArguments().getString("url");
        mPresenter = new NewsListPresenter(this);
        mPresenter.getItems(mUrl);

        startLoadingAnim();
        initRecycleView();
    }

    public void initRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // 设置空数据源，否则异步请求还没获取到数据就加载了recycleview
        NewsListAdapter adapter = new NewsListAdapter(getContext(), mData);
        adapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(String url) {
                Intent i = new Intent(getActivity(), NewsWebActivity.class);
                i.putExtra("url", url);
                startActivity(i);
            }
        });
        mRv.setAdapter(adapter);
    }

    public void startLoadingAnim() {
        mLoadingLayout = mLayout.findViewById(R.id.loading_view);
        loadingIv = mLoadingLayout.findViewById(R.id.loading_iv);
        ((AnimationDrawable) loadingIv.getDrawable()).start();

    }

    public void stopLoadingAnim() {
        ((AnimationDrawable) loadingIv.getDrawable()).stop();
        mLoadingLayout.setVisibility(View.GONE);
    }

    /**
     * 刷新list
     * @param list
     */
    public void refreshRecycleView(ArrayList<PictureNews> list) {
        mData.clear();
        mData.addAll(list);
        mRv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void OnGetNewsItemSuccess(ArrayList<PictureNews> newsList) {
        Message message = handler.obtainMessage(REQUEST_NEWS_ITEM_SUCCESS);
        message.obj = newsList;
        handler.sendMessage(message);
    }

    @Override
    public void OnGetNewsItemFailed() {
        handler.sendEmptyMessage(REQUEST_NEWS_ITEM_FAILED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
