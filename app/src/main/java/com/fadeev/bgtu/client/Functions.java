package com.fadeev.bgtu.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import  android.net.Uri;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;

public class Functions {
    public static String generateHash(String input) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte anEncodedHash : encodedHash) {
            hexString.append(Integer.toHexString(0xff & anEncodedHash));
        }
        Log.d("Generated hash", hexString.toString());
        return hexString.toString();
    }

    public static String getSharedToken(Context context){
        SharedPreferences sPref;
        sPref = context.getSharedPreferences(Constants.PREFERENCES.MAIN, Context.MODE_PRIVATE);
        return sPref.getString(Constants.PREFERENCES.TOKEN, "");
    }

    public static String getSharedUsername(Context context){
        SharedPreferences sPref;
        sPref = context.getSharedPreferences(Constants.PREFERENCES.MAIN, Context.MODE_PRIVATE);
        return sPref.getString(Constants.PREFERENCES.USERNAME, "");
    }

    public static String getMimeType(String url){
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if(extension != null) type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return type;
    }

    public static String getType(String url){
        return url.substring(url.lastIndexOf("."));
    }

    public static boolean writeResponseBodyToDisk(Context context, ResponseBody body, String fileName){
        try{
            File file = new File(context.getExternalFilesDir(null) + File.separator + fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true){
                    int read = inputStream.read(fileReader);
                    if(read == -1) break;
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("Download", "загружено: " + fileSizeDownloaded + " байт (" +
                            fileSizeDownloaded/1024 + " кБайт)" );
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if(inputStream != null){
                    inputStream.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
            }
        } catch (Exception e){
            return false;
        }
    }

    public static Drawable resize(Context context, Drawable image, int size){
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b,size,size,false);
        return new BitmapDrawable(context.getResources(),bitmapResized);
    }

    public static boolean checkAvatar(Context context){
        try {
            File file = new File(context.getExternalFilesDir(null) + File.separator + Constants.FILES.AVATAR);
            return file.exists();
        }
        catch (Exception e){
            return false;
        }
    }

    public static String getFileExtension(File file){
        String name = file.getName();
        return name.substring(name.lastIndexOf(".")).substring(1);
    }

    public static Uri createDownloadUri(Context c, String src){
        return Uri.parse(Constants.URL.FILES + "username=" + Functions.getSharedUsername(c) +
                "&token=" + Functions.getSharedToken(c) + "&src=" + src);
    }

    public static int getSharedTheme(Context context){
        SharedPreferences sPref;
        boolean nightTheme;
        String size;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        nightTheme = sPref.getBoolean(Constants.PREFERENCES.NIGHT_MODE, false);
        size = sPref.getString(Constants.PREFERENCES.TEXT_SIZE, "2");


        if(nightTheme){
            if (Objects.equals(size, "1")) return R.style.AppTheme_NightTheme_Small;
            if (Objects.equals(size, "3")) return R.style.AppTheme_NightTheme_Big;
            return R.style.AppTheme_NightTheme_Normal;
        }
        else
        {
            if (Objects.equals(size, "1")) return R.style.AppTheme_DefaultTheme_Small;
            if (Objects.equals(size, "3")) return R.style.AppTheme_DefaultTheme_Big;
            return R.style.AppTheme_DefaultTheme_Normal;
        }
    }

    public static int getSharedColor(Context context){
        SharedPreferences sPref;
        boolean nightTheme;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        nightTheme = sPref.getBoolean(Constants.PREFERENCES.NIGHT_MODE, false);

        if(nightTheme)return R.color.toolbar_night;
        return R.color.toolbar_default;
    }

    public static void setLocale(Context context){
        SharedPreferences sPref;
        String lang;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        lang = sPref.getString(Constants.PREFERENCES.LANGUAGE, "ru");

        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        res.updateConfiguration(conf, dm);



//
//        Locale locale = new Locale(lang);
//        Locale.setDefault(locale);
//        Configuration config = context.getResources().getConfiguration();
//        config.locale = locale;
//        context.getResources().updateConfiguration(config,
//                context.getResources().getDisplayMetrics());








//        Locale locale = new Locale(lang);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
//
//        SharedPreferences sPref= PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.putString(Constants.PREFERENCES.LANGUAGE,lang);
//        ed.apply();
    }

}
