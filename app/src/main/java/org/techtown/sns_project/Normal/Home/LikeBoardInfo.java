package org.techtown.sns_project.Normal.Home;

import java.io.Serializable;

public class LikeBoardInfo {

        String publisher;
        String ImgURL;
        String descrpition;

        public LikeBoardInfo(String publisher,  String ImgURL,String descrpition) {
            this.publisher = publisher;
            this.ImgURL = ImgURL;
            this.descrpition = descrpition;
        }

        public LikeBoardInfo(){
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


