package org.techtown.sns_project.Camera;

public class CodiDTO {

    private String title;
    private String brand;
    private String imageUrl;
    private String price;

    public CodiDTO() {
        this.title = title;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.price = price;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getBrand() {
        return brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}