package com.example.administrator.sohunews.presenter;

import com.example.administrator.sohunews.contract.INewsListContract;
import com.example.administrator.sohunews.model.GetNewsItemModel;
import com.example.administrator.sohunews.model.OnGetNewsItemListener;
import com.example.administrator.sohunews.model.bean.PictureNews;

import java.util.ArrayList;

public class NewsListPresenter implements INewsListContract.Presenter {

    private INewsListContract.View mView;
    private GetNewsItemModel mModel;

    public NewsListPresenter(INewsListContract.View view) {
        this.mView = view;
        this.mModel = new GetNewsItemModel();
    }

    @Override
    public void getItems(String url) {
        mModel.requestNewsItem(url, new OnGetNewsItemListener() {
            @Override
            public void onSuccess(ArrayList<PictureNews> list) {
                mView.OnGetNewsItemSuccess(list);
            }

            @Override
            public void onFailed() {
                mView.OnGetNewsItemFailed();
            }
        });
    }
}
