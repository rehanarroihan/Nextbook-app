package id.sch.smktelkom_mlg.nextbook.Util;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Rehan on 15/11/2017.
 */

public class AppController extends Application {
    private static final String TAG = AppController.class.getSimpleName();
    private static AppController instance;
    RequestQueue mRequestQuee;

    public static synchronized AppController getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private RequestQueue getmRequestQuee() {
        if (mRequestQuee == null) {
            mRequestQuee = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQuee;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQuee().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getmRequestQuee().add(req);
    }

    public void cancelAllRequest(Object req) {
        if (mRequestQuee != null) {
            mRequestQuee.cancelAll(req);
        }
    }
}
