package id.sch.smktelkom_mlg.nextbook.Model;

/**
 * Created by Rehan on 20/11/2017.
 */

public class Card {
    String card_id, uid, card_name, card_desc, color, card_dt, status;

    public Card() {
    }

    public Card(String card_id, String uid, String card_name, String card_desc, String color, String card_dt, String status) {
        this.card_id = card_id;
        this.uid = uid;
        this.card_name = card_name;
        this.card_desc = card_desc;
        this.color = color;
        this.card_dt = card_dt;
        this.status = status;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_desc() {
        return card_desc;
    }

    public void setCard_desc(String card_desc) {
        this.card_desc = card_desc;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCard_dt() {
        return card_dt;
    }

    public void setCard_dt(String card_dt) {
        this.card_dt = card_dt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
