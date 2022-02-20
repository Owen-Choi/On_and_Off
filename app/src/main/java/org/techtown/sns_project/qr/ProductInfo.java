package org.techtown.sns_project.qr;

public class ProductInfo {
    String URL;
    String SimilarURL;
    String Title;
    String Info;
    String Price;

    public ProductInfo(String URL, String similarURL, String title, String info, String price) {
        this.URL = URL;
        SimilarURL = similarURL;
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

    public String getSimilarURL() {
        return SimilarURL;
    }

    public void setSimilarURL(String similarURL) {
        SimilarURL = similarURL;
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
