package com.example.appbiblioteca.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTask extends AsyncTask<String, Long, String> {

    //variables del hilo
    private Context httpContext;
    private Asynchtask callback = null;
    ProgressDialog progressDialog;
    public String result = "";
    public String link_api = "";
    public String json_body = "";
    public String metodo_request = "";

    //constructor del hilo (Asynctask)
    public ServicioTask(Context ctx, String metodo_request, String link_api, String json_body, Asynchtask callback){
        this.httpContext = ctx;
        this.metodo_request = metodo_request;
        this.link_api = link_api;
        this.json_body = json_body;
        this.callback = callback;
    }

    public ServicioTask(Context ctx, String metodo_request, String link_api, Asynchtask callback){
        this.httpContext = ctx;
        this.metodo_request = metodo_request;
        this.link_api = link_api;
        this.callback = callback;
    }

   /* @Override
    protected String doInBackground(String... strings) {
        return null;
    }*/

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = ProgressDialog.show(httpContext, "Procesando Solicitud", "por favor, espere");
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        URL url = null;
        String linea = "";
        BufferedReader in;
        OutputStream os;
        StringBuffer sb;
        BufferedWriter writer;
        HttpURLConnection urlConnection;
        try {
            if(this.metodo_request == "GET"){
                // RL getUrl = new URL(url + "?" + param);
                url = new URL(link_api);
                urlConnection = (HttpURLConnection) url.openConnection();
                //DEFINIR PARAMETROS DE CONEXION
                /* milliseconds */
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod(metodo_request);
                urlConnection.connect();
            }else{
                url = new URL(link_api);
                urlConnection = (HttpURLConnection) url.openConnection();
                //DEFINIR PARAMETROS DE CONEXION
                /* milliseconds */
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod(metodo_request);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json; charset=utf-8");
                //OBTENER EL RESULTADO DEL REQUEST
                os = urlConnection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(json_body);
                writer.flush();
                writer.close();
                os.close();
            }
            int responseCode = urlConnection.getResponseCode();
            // conexion OK?
            if(responseCode == HttpURLConnection.HTTP_OK){
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                sb = new StringBuffer("");
                while ((linea = in.readLine()) != null){
                    sb.append(linea);
                    break;
                }
                in.close();
                result = sb.toString();
            }
            else{
                result = "Error: "+ responseCode;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        //progressDialog.dismiss(); se oculta el mensaje de carga de espera
        try {
            callback.processFinish(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}