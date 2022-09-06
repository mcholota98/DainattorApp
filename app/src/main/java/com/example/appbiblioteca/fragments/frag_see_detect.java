package com.example.appbiblioteca.fragments;

import com.example.appbiblioteca.MainActivity;
import com.example.appbiblioteca.Modelos.HiloWebService;
import com.example.appbiblioteca.Modelos.IConfiguSistema;
import com.example.appbiblioteca.Modelos.see_detect;
import com.example.appbiblioteca.R;
import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.SmoothLinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class frag_see_detect extends Fragment implements IConfiguSistema {

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
    int variable=1;
    Button btnBusca;
    EditText cadenaS;
    PlaceHolderView phv_detect;
    String url1;
    String str;
    int paraSaber=0;

    public frag_see_detect() {
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
    public static frag_see_detect newInstance(String param1, String param2) {
        frag_see_detect fragment = new frag_see_detect();
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
        View rootView = inflater.inflate(R.layout.fragment_see_detect, container, false);
        phv_detect = (PlaceHolderView)rootView.findViewById(R.id.phv_Detect);
        btnBusca=(Button) rootView.findViewById(R.id.btnBuscarS);
        cadenaS=(EditText) rootView.findViewById(R.id.txtBuscarS);
        phv_detect.setHasFixedSize(true);
        swSistema=(Switch) rootView.findViewById(R.id.schtCamera);
        phv_detect.setLayoutManager(new SmoothLinearLayoutManager(getContext()));
        variable=1;
        url1 = MainActivity.Burl.getString("key1");
        str = MainActivity.mMyAppsBundle.getString("key");
        showDialog();
        HiloWebService peticion1 = new HiloWebService("Historial", getContext(), "GET", url1+"monitoreo/historial/?cuidador_id="+str, this);
        HiloWebService peticion2 = new HiloWebService("EstadoSistema", getContext(), "GET", url1 + "monitoreo/vigilancia/", this);
        Thread hilo1 = new Thread(peticion1);
        Thread hilo2 = new Thread(peticion2);
        hilo1.start();
        hilo2.start();
        progDailog.dismiss();
        swSistema.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                //swSistema.setChecked(!(swSistema.isChecked()));
                variable=0;
                if(isChecked){
                    try {
                        JSONObject json_mensaje = new JSONObject();
                        json_mensaje.put("vigilancia", true);
                        swVigilancia(url1 + "monitoreo/vigilancia/",json_mensaje);
                    }catch (Exception ex)
                    {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    try {
                        JSONObject json_mensaje = new JSONObject();
                        json_mensaje.put("vigilancia", false);
                        swVigilancia(url1 + "monitoreo/vigilancia/",json_mensaje);
                    }catch (Exception ex)
                    {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                variable=1;
                try {
                    int nro1 = Integer.parseInt(cadenaS.getText().toString());
                    link_api = url1+"monitoreo/historial/?persona__cedula="+cadenaS.getText()+"&cuidador_id="+str;
                    mtBuscar(link_api);
                    showDialog();
                }catch (Exception e)
                {
                    link_api = url1+"monitoreo/historial/?nombres_apellidos="+cadenaS.getText()+"&cuidador_id="+str;
                    mtBuscar(link_api);
                    showDialog();
                }
            }
        });
        return rootView;
    }

    public void swVigilancia(String cadena, JSONObject jsonObject)
    {
        HiloWebService peticion2 = new HiloWebService("EstadoSistema", getContext(),"PUT", jsonObject.toString(), cadena, this);
        Thread hilo2 = new Thread(peticion2);
        hilo2.start();
    }

    public void mtBuscar(String cadena)
    {
        HiloWebService peticion1 = new HiloWebService("Historial", getContext(), "GET", cadena, this);
        Thread hilo1 = new Thread(peticion1);
        hilo1.start();
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
    public void getHistorial(String result) throws JSONException {
        try {
            this.phv_detect.removeAllViews();
            progDailog.dismiss();
            JSONObject json_data = new JSONObject(result);
            JSONArray json_historial = json_data.getJSONArray("historial");
            for (int i = 0; i < json_historial.length(); i++) {
                JSONObject un_historial = json_historial.getJSONObject(i);
                this.phv_detect.addView(new see_detect(getContext(), un_historial));
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getEstadoSistem(String result) throws JSONException {
        try {
            progDailog.dismiss();
            JSONObject json_data = new JSONObject(result);
            if (json_data.getString("vigilancia").equals("true")) {
                Toast.makeText(getContext(), "Camera on", Toast.LENGTH_LONG).show();
                swSistema.setChecked(true);
            } else {
                Toast.makeText(getContext(), "Camera off", Toast.LENGTH_LONG).show();
                swSistema.setChecked(false);
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

