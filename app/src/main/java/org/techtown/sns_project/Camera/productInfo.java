package org.techtown.sns_project.Camera;

import java.io.Serializable;

public class productInfo implements Serializable {
    String URL;
    String ImgURL;
    String Title;
    String Info;
    String Price;
    Number Count;
    String EID;
    public productInfo(String URL, String imgurl, String title, String info, String price, Number count,String EID) {
        this.URL = URL;
        ImgURL = imgurl;
        Title = title;
        Info = info;
        Price = price;
        Count = count;
        this.EID = EID;
    }

    public productInfo() {
    }

    public String getURL() {
        return URL;
    }
    public Number getCount() {
        return Count;
    }
    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImgURL() {
        return ImgURL;
    }
    public String getEID() {
        return EID;
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
