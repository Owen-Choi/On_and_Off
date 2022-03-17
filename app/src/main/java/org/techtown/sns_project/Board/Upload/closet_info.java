package org.techtown.sns_project.Board.Upload;

public class closet_info {
    String brand, img_url, name, url;

    public closet_info(String brand, String img_url, String name, String url) {
        this.brand = brand;
        this.img_url = img_url;
        this.name = name;
        this.url = url;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
