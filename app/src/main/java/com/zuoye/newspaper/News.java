package com.zuoye.newspaper;

public class News {
    private String newsId;          // 新闻唯一ID
    private String title;           // 标题
    private String date;            // 时间
    private String thumbnailUrl;    // 图片链接
    private String newsUrl;         // 新闻链接

    public News(String newsId, String title, String date, String thumbnailUrl, String newsUrl) {
        this.newsId = newsId;
        this.title = title;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.newsUrl = newsUrl;
    }
    public News(String title, String date, String thumbnailUrl) {
        this.title = title;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
