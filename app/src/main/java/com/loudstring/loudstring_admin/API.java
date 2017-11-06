package com.loudstring.loudstring_admin;

import android.content.Context;
import android.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Created by Asgard on 06-11-2017.
 */
public class API {

    private Context ctx;
    private String parent_url = "aHR0cDovL2FwcC5sb3Vkc3RyaW5nLmNvbS8";
    String text;
    public API(Context context)
    {
        this.ctx = context;
    }

    public String getApiUrl(String url)
    {
        String result ;
        result ="";
        try {
            // Receiving side
            byte[] data = Base64.decode(parent_url, Base64.DEFAULT);
            text = new String(data, StandardCharsets.UTF_8);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        switch(url)
        {
            case "user_login":
                result = text+"api/user_login.php?login=true";
                break;
            default:
                return result;
        }
        return result;
    }


}
