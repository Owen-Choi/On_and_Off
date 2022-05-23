package org.techtown.sns_project.qr;

import java.io.Serializable;

public class ProductInfo implements Serializable {
    String URL;
    String ImgURL;
    String Title;
    String Info;
    String Price;
    int Count;

    public ProductInfo(String URL, String ImgURL, String title, String info, String price) {
        this.URL = URL;
        this.ImgURL = ImgURL;
        Title = title;
        Info = info;
        this.Price = price;
    }

    public ProductInfo() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String ImgURL) {
        this.ImgURL = ImgURL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
