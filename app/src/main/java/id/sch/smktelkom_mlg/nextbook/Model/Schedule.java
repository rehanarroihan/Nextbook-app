package id.sch.smktelkom_mlg.nextbook.Model;

/**
 * Created by Rehan on 30/12/2017.
 */

public class Schedule {
    String scheduleid, classid, lessonid, day, start, end, lesson, teacher;

    public Schedule() {
    }

    public Schedule(String scheduleid, String classid, String lessonid, String day, String start, String end, String lesson, String teacher) {
        this.scheduleid = scheduleid;
        this.classid = classid;
        this.lessonid = lessonid;
        this.day = day;
        this.start = start;
        this.end = end;
        this.lesson = lesson;
        this.teacher = teacher;
    }

    public String getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(String scheduleid) {
        this.scheduleid = scheduleid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
