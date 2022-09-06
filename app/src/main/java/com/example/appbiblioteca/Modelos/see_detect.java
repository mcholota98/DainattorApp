package com.example.appbiblioteca.Modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbiblioteca.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONException;
import org.json.JSONObject;

@NonReusable
@Layout(R.layout.see_detect)
public class see_detect {

    @View(R.id.txtestado)
    TextView txtnumEvidencia;

    @View(R.id.imgEstado)
    ImageView imagen;

    Context contexto;
    JSONObject unaEvidencia;
    String base64Image;
    byte[] decodedString;
    Bitmap decodedByte;
    DecoderImagen decoder;

    public see_detect(Context contexto, JSONObject unaEvidencia) {
        this.contexto = contexto;
        this.unaEvidencia = unaEvidencia;
    }

    @Resolve
    protected void onResolved(){ ;
        try{
            this.txtnumEvidencia.setText(unaEvidencia.getString("custodiado__persona__nombres")+" - "+unaEvidencia.getString("expresion_facial")+" - Date: "+unaEvidencia.getString("fecha_hora"));
            if(!unaEvidencia.getString("imagen_expresion").equals("null")&&!unaEvidencia.getString("imagen_expresion").equals("")) {
                decoder = new DecoderImagen(unaEvidencia.getString("imagen_expresion"));
                this.imagen.setImageBitmap(decoder.getImagen());
            }
        }catch (JSONException ex){
            Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

