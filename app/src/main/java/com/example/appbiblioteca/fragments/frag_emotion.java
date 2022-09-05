package com.example.appbiblioteca.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.appbiblioteca.Global;
import com.example.appbiblioteca.MainActivity;
import com.example.appbiblioteca.Modelos.emotionR;
import com.example.appbiblioteca.Modelos.people_to_monitor;
import com.example.appbiblioteca.Modelos.see_detect;
import com.example.appbiblioteca.R;
import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class frag_emotion extends Fragment implements Asynchtask {

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
    TextView txtpresen;
    TextView txtorden;
    TextView cadenaC;
    PlaceHolderView phv_detect1;
    Button btnBus;
    String url1;

    public frag_emotion() {
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
    public static frag_emotion newInstance(String param1, String param2) {
        frag_emotion fragment = new frag_emotion();
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
        View rootView = inflater.inflate(R.layout.fragment_emotion, container, false);
        phv_detect1 = (PlaceHolderView)rootView.findViewById(R.id.phv_grafica);
        btnBus=(Button) rootView.findViewById(R.id.btnBuscarC);
        cadenaC=(EditText) rootView.findViewById(R.id.txtBuscarC);
        url1 = MainActivity.Burl.getString("key1");
        String str = MainActivity.mMyAppsBundle.getString("key");
        link_api = url1+"monitoreo/grafico/?cuidador_id="+str;
        ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, this);
        servicioTask.execute();
        showDialog();
        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int nro1 = Integer.parseInt(cadenaC.getText().toString());
                    link_api = url1+"monitoreo/grafico/?persona__cedula="+cadenaC.getText()+"&cuidador_id="+str;
                    ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, frag_emotion.this::processFinish);
                    servicioTask.execute();
                    showDialog();
                }catch (Exception e)
                {
                    link_api = url1+"monitoreo/grafico/?nombres_apellidos="+cadenaC.getText()+"&cuidador_id="+str;
                    ServicioTask servicioTask = new ServicioTask(getContext(), "GET", link_api, frag_emotion.this::processFinish);
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
            this.phv_detect1.removeAllViews();
            progDailog.dismiss();
            JSONObject json_data = new JSONObject(result);
            JSONArray json_historial = json_data.getJSONArray("grafico");
            for(int i = 0; i < json_historial.length(); i++){
                JSONObject un_historial = json_historial.getJSONObject(i);
                this.phv_detect1.addView(new emotionR(getContext(), un_historial));
            }
        }catch (JSONException ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

