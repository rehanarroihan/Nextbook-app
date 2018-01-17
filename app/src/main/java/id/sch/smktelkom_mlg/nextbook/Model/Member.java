package id.sch.smktelkom_mlg.nextbook.Model;

/**
 * Created by Rehan on 29/12/2017.
 */

public class Member {
    String name, email, pict;

    public Member() {
    }

    public Member(String name, String email, String pict) {
        this.name = name;
        this.email = email;
        this.pict = pict;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }
}
