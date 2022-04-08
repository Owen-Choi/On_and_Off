package org.techtown.sns_project.fragment;

import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;

public class DataFormat {
    String ImageUrl, publisher, description,userid;
    int nrlikes;
    ArrayList<ProductInfo> list;

    public String getUserid() {
        return userid;
    }

    public int getNrlikes() {
        return nrlikes;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ProductInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ProductInfo> list) {
        this.list = list;
    }

    public DataFormat() {
    }

    public DataFormat(String imageUrl, String publisher, String description, ArrayList<ProductInfo> list,String userid) {
        ImageUrl = imageUrl;
        this.publisher = publisher;
        this.description = description;
        this.list = list;
        this.userid = userid;

    }
}
