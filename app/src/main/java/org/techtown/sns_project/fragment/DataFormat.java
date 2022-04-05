package org.techtown.sns_project.fragment;

import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;

public class DataFormat {
    String ImageUrl, publisher, description;
    int like;
    ArrayList<ProductInfo> list;

    public String getImageUrl() {
        return ImageUrl;
    }

    public int getLike() {
        return like;
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

    public DataFormat(String imageUrl, String publisher, String description, ArrayList<ProductInfo> list) {
        ImageUrl = imageUrl;
        this.publisher = publisher;
        this.description = description;
        this.list = list;


    }
}
