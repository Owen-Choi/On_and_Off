package org.techtown.sns_project.Closet;

public class Closet_info {

    private String name;
    private String brand;
    private String img_url;
    private String url;

    public Closet_info(String name, String brand, String img_url, String url) {
        this.name = name;
        this.brand = brand;
        this.img_url = img_url;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }


    @Override
    public String toString() {
        return "recyclerContents{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }
}
