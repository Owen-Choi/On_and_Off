package org.techtown.sns_project.Camera;

public class PostDTO {

    private String userid;
    private String description;
    private String imageUrl;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PostDTO(String userid, String description, String imageUrl) {
        this.userid = userid;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}