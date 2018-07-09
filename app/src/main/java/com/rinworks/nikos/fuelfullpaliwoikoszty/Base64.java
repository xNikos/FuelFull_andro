package com.rinworks.nikos.fuelfullpaliwoikoszty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Base64 {
    public static String encode(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 5, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decode(String input) {
        byte[] decodedByte = android.util.Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
