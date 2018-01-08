package id.sch.smktelkom_mlg.nextbook.Model;

/**
 * Created by Rehan on 29/12/2017.
 */

public class Member {
    String name, pp, email, prov, pps;

    public Member() {
    }

    public Member(String name, String pp, String email, String prov, String pps) {
        this.name = name;
        this.pp = pp;
        this.email = email;
        this.prov = prov;
        this.pps = pps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getPps() {
        return pps;
    }

    public void setPps(String pps) {
        this.pps = pps;
    }
}
