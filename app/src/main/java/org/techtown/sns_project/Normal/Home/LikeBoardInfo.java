package org.techtown.sns_project.Normal.Home;

import java.io.Serializable;

public class LikeBoardInfo {

    public String publisher;
    public String ImgURL;
    public String descrpition;
    public int like=0;
        public LikeBoardInfo(String publisher,  String ImgURL,String descrpition,int like) {
            this.publisher = publisher;
            this.ImgURL = ImgURL;
            this.descrpition = descrpition;
            this.like = like;
        }

        public LikeBoardInfo(){
        }

        public int getLike() {
        return like;
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


