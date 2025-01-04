package com.example.madprojectg3;

public class Article {
    private int id;
    private String keyword;
    private String url;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl;
    public Article(int id, String keyword, String photoUrl, String url) {
        this.id = id;
        this.keyword = keyword;
        this.url = url;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                ", url='" + url + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
