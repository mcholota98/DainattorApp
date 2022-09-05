package com.example.appbiblioteca.fragments;

import com.example.appbiblioteca.Global;
import com.example.appbiblioteca.MainActivity;
import com.example.appbiblioteca.Modelos.DecoderImagen;
import com.example.appbiblioteca.Modelos.people_to_monitor;
import com.example.appbiblioteca.R;
import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import android.net.Uri;


public class frag_tutor extends Fragment implements Asynchtask {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String link_api;
    private ProgressDialog progDailog;
    private Switch swSistema;
    private JSONObject json_data;
    ImageView imagen;
    TextView nomUsuario;
    TextView pnombre;
    TextView papellidos;
    TextView pcedula;
    TextView pfecha;
    DecoderImagen decoder;
    String url1;

    public frag_tutor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ajustes.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_tutor newInstance(String param1, String param2) {
        frag_tutor fragment = new frag_tutor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tutor, container, false);
        imagen=(ImageView) rootView.findViewById(R.id.imagenUse);
        Button btnImagen = (Button) rootView.findViewById(R.id.btnCamar);
        nomUsuario=(TextView)rootView.findViewById(R.id.txtuserT);
        pnombre=(TextView)rootView.findViewById(R.id.txtnombre);
        papellidos=(TextView)rootView.findViewById(R.id.txtapellido);
        pcedula=(TextView)rootView.findViewById(R.id.txtcedula);
        pfecha=(TextView)rootView.findViewById(R.id.txtfecha);
        url1 = MainActivity.Burl.getString("key1");
        String str = MainActivity.mMyAppsBundle.getString("key");
        link_api = url1+"persona/cuidador/?id="+str;
        ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, this);
        servicioTask.execute();
        showDialog();
        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), 121);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 121) {
                Uri path = data.getData();
                imagen.setImageURI(path);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lo lamento no se a cargado una imagen " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showDialog(){
        progDailog = new ProgressDialog(getContext());
        progDailog.setTitle("Query data");
        progDailog.setMessage("please wait...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }
    @Override
    public void processFinish(String result) throws JSONException {
        try {
            progDailog.dismiss();
            JSONObject json_data = new JSONObject(result);
            JSONArray json_historial = json_data.getJSONArray("cuidadores");
            for(int i = 0; i < json_historial.length(); i++){
                JSONObject un_historial = json_historial.getJSONObject(i);
                nomUsuario.setText(un_historial.getString("nom_usuario"));
                pnombre.setText(un_historial.getString("persona__nombres"));
                papellidos.setText(un_historial.getString("persona__apellidos"));
                pcedula.setText(un_historial.getString("persona__cedula"));
                pfecha.setText(un_historial.getString("persona__fecha_nacimiento"));
                if(!un_historial.getString("persona__foto_perfil").equals("null")) {
                    decoder = new DecoderImagen(un_historial.getString("persona__foto_perfil"));
                    this.imagen.setImageBitmap(decoder.getImagen());
                }
            }
        }catch (JSONException ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}