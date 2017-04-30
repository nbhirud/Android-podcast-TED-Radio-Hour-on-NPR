package com.nbhirud.tedradiohouronnpr;

/**
 * Created by sumeesh on 18/06/16.
 */
public class Episode {

    String title, description, pubDate, imgUrl, duration, mp3Url;

    public Episode(String title, String description, String pubDate, String imgUrl, String duration, String mp3Url) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.imgUrl = imgUrl;
        this.duration = duration;
        this.mp3Url = mp3Url;
    }

    public Episode() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }
}
