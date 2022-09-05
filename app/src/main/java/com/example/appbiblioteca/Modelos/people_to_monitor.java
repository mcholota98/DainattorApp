package com.example.appbiblioteca.Modelos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.appbiblioteca.Global;
import com.example.appbiblioteca.MainActivity;
import com.example.appbiblioteca.R;

import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;
import com.example.appbiblioteca.editCustodiado;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@NonReusable
@Layout(R.layout.people_to_monitor)
public class people_to_monitor implements Asynchtask {
    private String link_api;
    private ProgressDialog progDailog;
    String url1;
    public static Bundle mMyAppsBundle1 = new Bundle();

    @View(R.id.txtfrase)
    TextView txtnumEvidencia;

    @View(R.id.imgEvidencia)
    ImageView imagen;

    Context contexto;
    JSONObject unaEvidencia;
    String base64Image;
    byte[] decodedString;
    Bitmap decodedByte;
    DecoderImagen decoder;
    Intent changeActivity;
    Bundle b;

    public people_to_monitor(Context contexto, JSONObject unaEvidencia) {
        this.contexto = contexto;
        this.unaEvidencia = unaEvidencia;
    }

    @Resolve
    protected void onResolved(){ ;
        try{
            this.txtnumEvidencia.setText(unaEvidencia.getString("persona__nombres")+" "+unaEvidencia.getString("persona__apellidos")+" - Date of Birth: "+unaEvidencia.getString("persona__fecha_nacimiento"));
            if(!unaEvidencia.getString("persona__foto_perfil").equals("null")&&!unaEvidencia.getString("persona__foto_perfil").equals("")) {
                decoder = new DecoderImagen(unaEvidencia.getString("persona__foto_perfil"));
                this.imagen.setImageBitmap(decoder.getImagen());
            }
        }catch (JSONException ex){
            Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Click(R.id.btnEdit)
    public void onClick_VerEvidencias() {
        try {
            Toast.makeText(contexto, unaEvidencia.getString("persona__nombres"), Toast.LENGTH_LONG).show();
            changeActivity = new Intent(this.contexto, editCustodiado.class);
            changeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            b = new Bundle();
            b.putString("id", this.unaEvidencia.getString("id"));
            b.putString("cuidador_id", this.unaEvidencia.getString("cuidador_id"));
            b.putString("persona_id", this.unaEvidencia.getString("persona_id"));
            b.putString("persona__nombres", this.unaEvidencia.getString("persona__nombres"));
            b.putString("persona__apellidos", this.unaEvidencia.getString("persona__apellidos"));
            b.putString("persona__cedula", this.unaEvidencia.getString("persona__cedula"));
            b.putString("persona__fecha_nacimiento", this.unaEvidencia.getString("persona__fecha_nacimiento"));
            b.putString("persona__foto_perfil", this.unaEvidencia.getString("persona__foto_perfil"));
            changeActivity.putExtras(b);
            contexto.startActivity(changeActivity);
        } catch (Exception e) {
            Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showDialog(){
        progDailog = new ProgressDialog(this.contexto);
        progDailog.setTitle("Consultando datos");
        progDailog.setMessage("por favor, espere...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Click(R.id.btnCamara_cus)
    public void onClick_Camara() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this.contexto);
        dialogo1.setTitle("Important");
        dialogo1.setMessage("Make sure you are positioned at least 50 cm and no more than one meter away from the camera");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
               Aceptar();
            }
        });
        dialogo1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }

    public void Aceptar()
    {
        try {
            Toast.makeText(contexto, unaEvidencia.getString("persona__nombres"), Toast.LENGTH_LONG).show();
            JSONObject json_mensaje = new JSONObject();
            json_mensaje.put("persona_id", unaEvidencia.getString("persona_id"));
            url1 = MainActivity.Burl.getString("key1");
            link_api = url1+"monitoreo/entrenamiento-facial/";
            ServicioTask servicioTask = new ServicioTask(this.contexto, "PUT", link_api,json_mensaje.toString(),this);
            servicioTask.execute();
            showDialog();
        } catch (Exception e) {
            Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void processFinish(String result) throws JSONException {
        try {
            progDailog.dismiss();
            JSONObject json_historial = new JSONObject(result);
            Toast.makeText(contexto, json_historial.getString("entrenamiento_facial"), Toast.LENGTH_LONG).show();
            }catch (JSONException ex){
            Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

