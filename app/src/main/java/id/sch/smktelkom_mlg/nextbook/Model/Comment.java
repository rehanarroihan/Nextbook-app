package id.sch.smktelkom_mlg.nextbook.Model;

/**
 * Created by rehan on 15/01/18.
 */

public class Comment {
    String commentid, uid, createds, comment, pict, dspname;

    public Comment() {
    }

    public Comment(String commentid, String uid, String createds, String comment, String pict, String dspname) {
        this.commentid = commentid;
        this.uid = uid;
        this.createds = createds;
        this.comment = comment;
        this.pict = pict;
        this.dspname = dspname;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreateds() {
        return createds;
    }

    public void setCreateds(String createds) {
        this.createds = createds;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public String getDspname() {
        return dspname;
    }

    public void setDspname(String dspname) {
        this.dspname = dspname;
    }
}
