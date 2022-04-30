package org.techtown.sns_project.Board.profile;

public class MyProfile_info {

    String publisher;
    String ImgURL;
    String descrpition;

    public MyProfile_info(String publisher, String imgURL, String descrpition) {
        this.publisher = publisher;
        ImgURL = imgURL;
        this.descrpition = descrpition;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getDescrpition() {
        return descrpition;
    }

    public void setDescrpition(String descrpition) {
        this.descrpition = descrpition;
    }
}
