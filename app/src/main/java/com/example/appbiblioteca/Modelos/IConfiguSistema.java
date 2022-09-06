package com.example.appbiblioteca.Modelos;

import org.json.JSONException;

public interface IConfiguSistema {
    void getHistorial(String result) throws JSONException;
    void getEstadoSistem(String result) throws JSONException;
}
