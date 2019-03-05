package com.example.administrator.sohunews.model;

import com.example.administrator.sohunews.config.Constans;
import com.example.administrator.sohunews.model.bean.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetNewsTabModel {

    /**
     * Jsoup解析搜狐首页新闻分类标题(分类网页结构不统一)
     * @param listner
     */
    public void requestNewsModuleTitlesFromNet(final OnGetTabListener listner) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<News> list = new ArrayList<>();
                Document[] document = {null};
                try {
                    document[0] = Jsoup.connect(Constans.URL).timeout(5000).get();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (document[0] != null) {
                    Elements elements = document[0].getElementsByClass("head-nav").first().select("ul li");
                    // 头尾两个li单独处理
                    for (int i = 0; i < elements.size() ; i++) {
                        Element liElement = elements.get(i);
                        if (i == 0) {
                            News news = new News();
                            Element a = liElement.selectFirst("a");
                            news.setUrl(a.attr("href"));
                            news.setTitle(a.selectFirst("em.sohu-logo").text());
                            list.add(news);
                        } else if (i == elements.size() - 1) {
                            Elements moreLiElements = liElement.select("div a");
                            for (int j = 0; j <moreLiElements.size(); j++) {
                                News news = new News();
                                Element moreAElement = moreLiElements.get(j);
                                news.setUrl(moreAElement.attr("href"));
                                news.setTitle(moreAElement.text());
                                list.add(news);
                            }
                        } else {
                            News news = new News();
                            news.setUrl(liElement.select("a").attr("href"));
                            news.setTitle(liElement.select("a").text());
                            list.add(news);
                        }
                    }
                    listner.onSuccess(list);
                }
            }
        }).start();
    }

    /**
     * 选取分类网页结构统一的
     * @param listner
     */
    public void requestNewsModuleTitlesFromLocal(final OnGetTabListener listner) {
        ArrayList<News> list = new ArrayList<>();
        for (int i = 0; i < Constans.TAB_LIST_TITLE.length; i++) {
            News news = new News(Constans.TAB_LIST_TITLE[i], Constans.TAB_LIST_URL[i]);
            list.add(news);
        }
        listner.onSuccess(list);
    }
}
