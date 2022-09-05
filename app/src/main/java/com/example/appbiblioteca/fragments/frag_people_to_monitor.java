package com.example.appbiblioteca.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.appbiblioteca.Global;
import com.example.appbiblioteca.MainActivity;
import com.example.appbiblioteca.Modelos.people_to_monitor;
import com.example.appbiblioteca.R;
import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;
import com.example.appbiblioteca.custodiado;
import com.example.appbiblioteca.menu;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.SmoothLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class frag_people_to_monitor extends Fragment implements Asynchtask {

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
    EditText cadena;
    PlaceHolderView phv_historial;
    Button btnadd;
    Button btnbuscar;
    Switch encender;
    String url1;

    public frag_people_to_monitor() {
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
    public static frag_people_to_monitor newInstance(String param1, String param2) {
        frag_people_to_monitor fragment = new frag_people_to_monitor();
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
        View rootView = inflater.inflate(R.layout.fragment_people_to_monitor, container, false);
        phv_historial = (PlaceHolderView)rootView.findViewById(R.id.phv_historial);
        btnadd = (Button) rootView.findViewById(R.id.btnAdd);
        btnbuscar = (Button) rootView.findViewById(R.id.btnBuscar);
        cadena=(EditText) rootView.findViewById(R.id.txtBuscar);
        encender=(Switch) rootView.findViewById(R.id.schtCamera);
        phv_historial.setHasFixedSize(true);
        phv_historial.setLayoutManager(new SmoothLinearLayoutManager(getContext()));
        url1 = MainActivity.Burl.getString("key1");
        String str = MainActivity.mMyAppsBundle.getString("key");
        link_api = url1+"persona/custodiado/?cuidador_id="+str;
        ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, this);
        servicioTask.execute();
        showDialog();

        //encender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         //   @Override
         //   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

         //   }
        //});

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getActivity(), custodiado.class);
                startActivity(newActivity);
            }
        });

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int nro1 = Integer.parseInt(cadena.getText().toString());
                    link_api = url1+"persona/custodiado/?persona__cedula="+cadena.getText()+"&cuidador_id="+str;
                    ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, frag_people_to_monitor.this::processFinish);
                    servicioTask.execute();
                    showDialog();
                }catch (Exception e)
                {
                    link_api = url1+"persona/custodiado/?nombres_apellidos="+cadena.getText()+"&cuidador_id="+str;
                    ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, frag_people_to_monitor.this::processFinish);
                    servicioTask.execute();
                    showDialog();
                }
            }
        });

        return rootView;
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
            this.phv_historial.removeAllViews();
            progDailog.dismiss();
            JSONObject json_data = new JSONObject(result);
            JSONArray json_historial = json_data.getJSONArray("custodiados");
            for(int i = 0; i < json_historial.length(); i++){
                JSONObject un_historial = json_historial.getJSONObject(i);
                this.phv_historial.addView(new people_to_monitor(getContext(), un_historial));
            }
        }catch (JSONException ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}


