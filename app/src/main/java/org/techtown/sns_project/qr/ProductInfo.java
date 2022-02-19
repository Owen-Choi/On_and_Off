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
}
