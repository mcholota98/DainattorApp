package com.example.appbiblioteca.Modelos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ImagenBitmap {
    byte[] decodedString;
    Bitmap decodedByte;
    String base64;
    ImageView img;
    public ImagenBitmap(String base64){
        this.base64 = base64;
    }
    public ImagenBitmap(ImageView img){
        this.img = img;
    }
    public Bitmap getImagen(){
        decodedString = Base64.decode(this.base64.split(",")[1], Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return  decodedByte;
    }
    public String getBase64() {
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            this.base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        return base64;
    }
}
