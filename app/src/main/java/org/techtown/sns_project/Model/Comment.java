package org.techtown.sns_project.Model;

public class Comment {
    private String comment;
    private String commentid;
    private String getuid;

    public Comment(String comment, String commentid, String getuid) {
        this.comment = comment;
        this.commentid = commentid;
        this.getuid = getuid;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }
    public String getGetuid() {
        return getuid;
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
