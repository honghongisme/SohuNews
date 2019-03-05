package com.example.administrator.sohunews.presenter;

import com.example.administrator.sohunews.contract.IMainContract;
import com.example.administrator.sohunews.model.GetNewsTabModel;
import com.example.administrator.sohunews.model.OnGetTabListener;
import com.example.administrator.sohunews.model.bean.News;

import java.util.ArrayList;

public class MainPresenter implements IMainContract.Presenter {

    private IMainContract.View mView;
    private GetNewsTabModel mModel;

    public MainPresenter(IMainContract.View view) {
        mView = view;
        mModel = new GetNewsTabModel();
    }

    @Override
    public void requestNewsModuleTitles() {
        mModel.requestNewsModuleTitlesFromLocal(new OnGetTabListener() {
            @Override
            public void onSuccess(ArrayList<News> titles) {
                mView.onGetNewsModuleTitlesSuccess(titles);
            }

            @Override
            public void onFailed() {
                mView.onGetNewsModuleTitlesFailed();
            }
        });

    }
}
