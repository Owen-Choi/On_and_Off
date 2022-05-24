package org.techtown.sns_project.Normal.Home;

public class QrListInfo {

    public String info;
    public String imgurl;
    public String title;
    public String eid;
    public int count;
    public String url;
        public QrListInfo(String info, String imgurl, String title, int count, String eid,String url) {
            this.info = info;
            this.imgurl = imgurl;
            this.title = title;
            this.count = count;
            this.eid = eid;
            this.url = url;
        }

        public QrListInfo(){
        }

        public int getcount() {
        return count;
    }

        public String getinfo() {
            return info;
        }

        public String getImgURL() {
            return imgurl;
        }

        public String gettitle() {
            return title;
        }

        public String geteid() {
        return eid;
    }

        public String getUrl(){return url;}
    }


