package com.example.appbiblioteca.Modelos;

import android.content.Context;
import android.os.AsyncTask;

import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;

import org.json.JSONException;

public class HiloWebService extends AsyncTask<String, Long, String> implements Runnable, Asynchtask {

    private String link_api;
    private String json_body;
    private String metodo_request;
    private Context contexto;
    private String accion;
    IConfiguSistema callback;

    public HiloWebService(String accion, Context contexto, String metodo_request, String json_body, String link_api, IConfiguSistema callback){
        this.accion = accion;
        this.contexto = contexto;
        this.metodo_request = metodo_request;
        this.json_body = json_body;
        this.link_api = link_api;
        this.callback = callback;
    }

    public HiloWebService(String accion, Context contexto, String metodo_request, String link_api, IConfiguSistema callback){
        this.accion = accion;
        this.contexto = contexto;
        this.metodo_request = metodo_request;
        this.link_api = link_api;
        this.callback = callback;
    }

    @Override
    public void run() {
        ServicioTask servicioTask = new ServicioTask(contexto, metodo_request, link_api, json_body, this);
        servicioTask.execute();
    }

    @Override
    public void processFinish(String result) throws JSONException {
        if (this.accion == "Historial"){
            callback.getHistorial(result);
        }else{
            callback.getEstadoSistem(result);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}

