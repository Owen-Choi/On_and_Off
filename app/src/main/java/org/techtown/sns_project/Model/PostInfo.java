package org.techtown.sns_project.Model;

public class PostInfo {

    String publisher;
    String ImgURL;
    String descrpition;

    public PostInfo(String publisher,  String ImgURL,String descrpition) {
        this.publisher = publisher;
        this.ImgURL = ImgURL;
        this.descrpition = descrpition;
    }

    public PostInfo(){
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public String getDescrpition() {
        return descrpition;
    }


}
