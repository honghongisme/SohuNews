package com.example.administrator.sohunews.model;

import com.example.administrator.sohunews.model.bean.News;

import java.util.ArrayList;

public interface OnGetTabListener {

    void onSuccess(ArrayList<News> titles);
    void onFailed();
}
