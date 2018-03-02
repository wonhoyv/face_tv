package obria.com.videotest.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ysj on 2018/2/27.
 */

public class SharedPreferencesHelper {

    private SharedPreferences mPref;
    private  Context context;

    public SharedPreferencesHelper(Context context) {
        this.context  = context;
        mPref = context.getSharedPreferences("ljy", Context.MODE_PRIVATE);
    }

    public void setStringValue(String tag,String value){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(tag,value);
        editor.commit();
    }

    public String getStringValue(String tag,String defaultValue){
        return  mPref.getString(tag,defaultValue);
    }
}
