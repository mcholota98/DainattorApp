package com.example.appbiblioteca;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Asynchtask {


    private TextView txtUsuario;
    private TextView txtClave;
    private ProgressDialog progDailog;
    public static JSONObject usuario;
    private TextView UrlGeneral;
    public static Bundle mMyAppsBundle = new Bundle();
    public static Bundle Burl = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtUsuario = (TextView) findViewById(R.id.txt_usuario);
        this.txtClave = (TextView) findViewById(R.id.txt_contras);
        this.UrlGeneral=(TextView) findViewById(R.id.txt_Url);
        UrlGeneral.setText("https://webservicestrastorno.herokuapp.com/");
    }
    public void login(View view) {
        try {
            MainActivity.Burl.putString("key1",UrlGeneral.getText().toString());
            JSONObject json_mensaje = new JSONObject();
            json_mensaje.put("usuario", txtUsuario.getText());
            json_mensaje.put("clave", txtClave.getText());
            ServicioTask servicioTask = new ServicioTask(this, "POST",UrlGeneral.getText().toString()+"persona/autenticacion/", json_mensaje.toString(), this);
            txtUsuario.setText("");
            txtClave.setText("");
            servicioTask.execute();
            progDailog = new ProgressDialog(this);
            progDailog.setTitle("Verifying user");
            progDailog.setMessage("please wait...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void NewCreate(View view) {
        MainActivity.Burl.putString("key1",UrlGeneral.getText().toString());
        Toast.makeText(this, UrlGeneral.getText().toString(), Toast.LENGTH_LONG).show();
        Intent newActivity = new Intent(MainActivity.this, tutor.class);
        startActivity(newActivity);
    }

    public static JSONObject getUsuario() {
        return usuario;
    }

    public void setUsuario(JSONObject usuario) {
        this.usuario = usuario;
    }

    @Override
    public void processFinish(String result) throws JSONException {
        try {
            JSONObject json_response = new JSONObject(result);
            progDailog.dismiss();
            if(json_response.has("usuario")){
                Intent newActivity = new Intent(MainActivity.this, menu.class);
                this.setUsuario(json_response.getJSONObject("usuario"));
                MainActivity.mMyAppsBundle.putString("key",this.getUsuario().get("id").toString());
                Toast.makeText(this, "Welcome " + this.getUsuario().get("nom_usuario").toString(), Toast.LENGTH_LONG).show();
                startActivity(newActivity);
            }else{
                Toast.makeText(this, json_response.get("usuario").toString(), Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            progDailog.dismiss();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}