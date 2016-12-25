package com.example.pratik.intouch_v_01.model;

/**
 * Created by pratik on 20-Nov-16.
 */

public class News {

    private String headline, news_content, image, date, time, category, source;
    private long newsid;

    public News() {
    }

    public News(String headline, String news_content, long newsid, String image, String date, String time, String category, String source) {
        this.headline = headline;
        this.news_content = news_content;
        this.newsid = newsid;
        this.image = image;
        this.date = date;
        this.time = time;
        this.category = category;
        this.source = source;
    }

    public String getHeadline() {
        return headline;
    }

    public String getNews_content() {
        return news_content;
    }

    public long getNewsid() {
        return newsid;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public String getSource() {
        return source;
    }
}
