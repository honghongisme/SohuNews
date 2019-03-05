package com.example.administrator.sohunews.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import com.example.administrator.sohunews.model.bean.PictureNews;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GetNewsItemModel {

    /**
     * 请求url里的新闻item
     * @param url
     * @param listener
     */
    public void requestNewsItem(final String url, final OnGetNewsItemListener listener) {
        new Thread(new Runnable() {
            Document document = null;
            ArrayList<PictureNews> list = new ArrayList<>();
            @Override
            public void run() {
                try {
                    document = Jsoup.connect(url).timeout(8000000).get();
                    if (document != null) {
                        Elements newsItemList = document.select("div[data-role=news-item]");
                        for (int i = 0; i < newsItemList.size(); i++) {
                            PictureNews news = new PictureNews();
                            Element newsItem = newsItemList.get(i);
                            news.setTitle(newsItem.selectFirst("h4 a").text());
                            news.setUrl("http:" + newsItem.selectFirst("h4 a").attr("href"));
                            news.setFrom(newsItem.selectFirst("div.other span.name").text());
                     //       news.setTime(newsItem.selectFirst("div.other span.time").text());
                            if (!newsItem.select("div.pic").isEmpty()) {
                                // 单张图片直接取
                                news.setImgUrl("http:" + newsItem.selectFirst("div.pic a img").attr("src"));
                            } else if (!newsItem.select("div.pic-group").isEmpty()){
                                // 组图片默认取第一张
                                news.setImgUrl("http:" + newsItem.selectFirst("div.pic-group ul li a img").attr("src"));
                            }
                            list.add(news);
                        }
                        listener.onSuccess(list);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailed();
                }

            }
        }).start();
    }

    public void requestNewsImage(final ArrayList<PictureNews> news) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < news.size(); i++) {
                    String url = news.get(i).getImgUrl();
                    try {
                        URL ur = new URL(url);
                        URLConnection connection = ur.openConnection();
                        InputStream in = connection.getInputStream();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        Bitmap bm = BitmapFactory.decodeStream(in, null, options);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }
}
