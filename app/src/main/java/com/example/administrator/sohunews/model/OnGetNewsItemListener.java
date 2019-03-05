package com.example.administrator.sohunews.model;

import com.example.administrator.sohunews.model.bean.PictureNews;

import java.util.ArrayList;

public interface OnGetNewsItemListener {

    void onSuccess(ArrayList<PictureNews> list);
    void onFailed();
}
