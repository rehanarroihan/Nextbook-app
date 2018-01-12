package id.sch.smktelkom_mlg.nextbook.Model;

/**
 * Created by Rehan on 9/01/2018.
 */

public class Post {
    String postid, dspname, prov, pict, picts, lesson, create, content, img, doc;
    Integer comment;

    public Post() {
    }

    public Post(String postid, String dspname, String prov, String pict, String picts,
                String lesson, String create, String content, String img, String doc, Integer comment) {
        this.postid = postid;
        this.dspname = dspname;
        this.prov = prov;
        this.pict = pict;
        this.picts = picts;
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

    public String getDspname() {
        return dspname;
    }

    public void setDspname(String dspname) {
        this.dspname = dspname;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public String getPicts() {
        return picts;
    }

    public void setPicts(String picts) {
        this.picts = picts;
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
