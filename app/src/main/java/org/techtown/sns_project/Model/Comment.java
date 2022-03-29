package org.techtown.sns_project.Model;

public class Comment {
    private String comment;
    private String commentid;

    public Comment(String comment, String commentid) {
        this.comment = comment;
        this.commentid = commentid;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }
}
