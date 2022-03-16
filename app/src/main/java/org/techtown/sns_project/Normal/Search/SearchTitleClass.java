package org.techtown.sns_project.Normal.Search;

public class SearchTitleClass {
    private String brand;
    private String title;
    private String url;
    public SearchTitleClass(String brand, String title, String url) {
        this.brand = brand;
        this.title = title;
        this.url = url;
    }

    public String getBrand() { return this.brand; }
    public String getTitle() {
        return this.title;
    }
    public String getUrl() {
        return this.url;
    }

}