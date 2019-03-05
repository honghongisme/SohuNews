package com.example.administrator.sohunews.contract;

import com.example.administrator.sohunews.model.bean.News;

import java.util.ArrayList;

public interface IMainContract {

    interface View {
        void onGetNewsModuleTitlesSuccess(ArrayList<News> titles);
        void onGetNewsModuleTitlesFailed();
    }

    interface Presenter {
        void requestNewsModuleTitles();
    }
}
