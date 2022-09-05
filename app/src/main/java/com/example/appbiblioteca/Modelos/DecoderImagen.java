package com.example.appbiblioteca.Modelos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class DecoderImagen {
    byte[] decodedString;
    Bitmap decodedByte;
    String base64;
    public DecoderImagen(String base64){
        this.base64 = base64;
    }
    public Bitmap getImagen(){
        decodedString = Base64.decode(this.base64.split(",")[1], Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return  decodedByte;
    }
}
