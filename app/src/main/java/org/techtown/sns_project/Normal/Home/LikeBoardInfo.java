package org.techtown.sns_project.Normal.Home;

import java.io.Serializable;

public class LikeBoardInfo {

    public String publisher;
    public String ImgURL;
    public String descrpition;
    public String document;
    public int nrlikes;

        public LikeBoardInfo(String publisher,  String ImgURL,String descrpition,int nrlikes, String document) {
            this.publisher = publisher;
            this.ImgURL = ImgURL;
            this.descrpition = descrpition;
            this.nrlikes = nrlikes;
            this.document = document;
        }

        public LikeBoardInfo(){
        }

        public int getNrlikes() {
        return nrlikes;
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

        public String getDocument() {
        return document;
    }
    }


