package id.sch.smktelkom_mlg.nextbook.Model;

/**
 * Created by Rehan on 9/01/2018.
 */

public class Post {
    String postid, uid, dspname, pict, lesson, create, content, img, doc;
    Integer comment;

    public Post() {
    }

    public Post(String postid, String uid, String dspname, String pict, String lesson, String create, String content, String img, String doc, Integer comment) {
        this.postid = postid;
        this.uid = uid;
        this.dspname = dspname;
        this.pict = pict;
        this.lesson = lesson;
        this.create = create;
        this.content = content;
        this.img = img;
        this.doc = doc;
        this.comment = comment;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDspname() {
        return dspname;
    }

    public void setDspname(String dspname) {
        this.dspname = dspname;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }
}
