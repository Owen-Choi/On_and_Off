package org.techtown.sns_project.qr;

public class ProductInfo {
    String URL;
    String ImgURL;
    String Title;
    String Info;
    String Price;

    public ProductInfo(String URL, String imgurl, String title, String info, String price) {
        this.URL = URL;
        ImgURL = imgurl;
        Title = title;
        Info = info;
        Price = price;
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

    public void setImgURL(String imgurl) {
        ImgURL = imgurl;
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
