package ru.rsdev.HorizontalGallery;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    public void setData(Context context, String name, String value){
        SharedPreferences sPref = context.getSharedPreferences("mysettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(name, value);
        ed.commit();
    }

    /*
                        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        dialogResultText = value.toString();
                        ed.putString(unicDateList.get(dayNumber), value.toString());
                        ed.commit();
     */


    public String getData(Context context, String name){
        SharedPreferences mSettings = context.getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        return mSettings.getString(name, "");
    }


}
