package org.techtown.sns_project.Model;

public class PostInfo {

    String publisher;
    String ImgURL;
    String descrpition;
    String postid;

    public PostInfo(String publisher,  String ImgURL,String descrpition,String postid) {
        this.publisher = publisher;
        this.ImgURL = ImgURL;
        this.descrpition = descrpition;
        this.postid = postid;
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
