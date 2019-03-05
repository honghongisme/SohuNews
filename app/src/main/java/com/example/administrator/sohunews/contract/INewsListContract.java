package com.example.administrator.sohunews.contract;

import com.example.administrator.sohunews.model.bean.PictureNews;

import java.util.ArrayList;

public interface INewsListContract {

    interface View {
        void OnGetNewsItemSuccess(ArrayList<PictureNews> newsList);
        void OnGetNewsItemFailed();
    }

    interface Presenter {
        void getItems(String url);
    }
}
