package com.pief.facampnetwork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Utilities {
    public static Bitmap getBitmap(String imageString){
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

    public static Bitmap getBitmap(byte[] imageBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

    public static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imagemBytes = byteArrayOutputStream.toByteArray();
        String imagemString = Base64.encodeToString(imagemBytes, Base64.DEFAULT);
        return imagemString;
    }

    public static String formatPrice(double preco){
        String precoString = "R$" + String.format("%.2f", preco);
        precoString = precoString.replace(".", ",");
        return precoString;
    }
}
