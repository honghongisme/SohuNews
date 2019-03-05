package com.example.administrator.sohunews.view;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.sohunews.R;
import com.example.administrator.sohunews.adapter.PageAdapter;
import com.example.administrator.sohunews.contract.IMainContract;
import com.example.administrator.sohunews.model.bean.News;
import com.example.administrator.sohunews.presenter.MainPresenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainContract.View {

    private static final int REQUEST_TITLE_SUCCESS = 0;
    private static final int REQUEST_TITLE_FAILED = 1;

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.tab)
    TabLayout mTabLayout;

    private ArrayList<NewsListFragment> fragments;
    private PageAdapter mAdapter;
    private MainPresenter mPresenter;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_TITLE_SUCCESS:
                    initTabTitle((ArrayList<News>) msg.obj);
                    break;
                case REQUEST_TITLE_FAILED:
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        fragments = new ArrayList<>();
        mPresenter = new MainPresenter(this);
        mPresenter.requestNewsModuleTitles();
    }

    /**
     * 初始化tab标题
     * @param titles
     */
    private void initTabTitle(ArrayList<News> titles) {
        for (int i = 0; i < titles.size(); i++) {
            NewsListFragment fragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", titles.get(i).getUrl());
            fragment.setArguments(bundle);
            fragments.add(fragment);
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewpager, false);
        mAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
        mViewpager.setAdapter(mAdapter);

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.getTabAt(i).setText(titles.get(i).getTitle());
        }
    }

    @Override
    public void onGetNewsModuleTitlesSuccess(ArrayList<News> titles) {
        Message message = handler.obtainMessage(REQUEST_TITLE_SUCCESS);
        message.obj = titles;
        handler.sendMessage(message);
    }

    @Override
    public void onGetNewsModuleTitlesFailed() {
        handler.sendEmptyMessage(REQUEST_TITLE_FAILED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
