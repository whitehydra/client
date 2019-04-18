package com.fadeev.bgtu.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import okhttp3.ResponseBody;

public class Functions {
    public static String generateHash(String input) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte anEncodedHash : encodedHash) {
            hexString.append(Integer.toHexString(0xff & anEncodedHash));
        }
        Log.d("Generated hash ", hexString.toString());
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
        if(extension != null){
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        }
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
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true){
                    int read = inputStream.read(fileReader);
                    if(read == -1) break;
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("Download", "file download: " + fileSizeDownloaded + " byte");
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



}
