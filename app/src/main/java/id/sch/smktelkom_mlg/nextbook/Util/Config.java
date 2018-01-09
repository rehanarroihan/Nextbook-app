package id.sch.smktelkom_mlg.nextbook.Util;

import android.content.Context;
import android.content.Intent;

import id.sch.smktelkom_mlg.nextbook.NoconActivity;

/**
 * Created by Rehan on 13/11/2017.
 */

public class Config {
    public static String ServerURL = "http://10.102.1.83/nextbook-api/";
    public static String ImageURL = "http://10.102.1.83/nextbook/assets/";

    public void timeout(Context context) {
        Intent it = new Intent(context.getApplicationContext(), NoconActivity.class);
        context.startActivity(it);
    }
}