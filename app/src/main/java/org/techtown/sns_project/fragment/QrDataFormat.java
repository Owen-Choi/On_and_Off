package org.techtown.sns_project.fragment;

import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;

public class QrDataFormat {
    public String info;
    public String imgURL;
    public String title;
    public String eid;
    public int count;
    public String url;

    public int getcount() {
        return count;
    }

    public String getinfo() {
        return info;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String gettitle() {
        return title;
    }

    public String geteid() {
        return eid;
    }

    public String getUrl(){return url;}

    public QrDataFormat() {
    }

}
