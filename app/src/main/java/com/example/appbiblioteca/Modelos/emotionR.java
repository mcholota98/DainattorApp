package com.example.appbiblioteca.Modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbiblioteca.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONException;
import org.json.JSONObject;

@NonReusable
@Layout(R.layout.emotion_r)
public class emotionR {

    @View(R.id.grafica_r)
    GraphView grafiquita;

    @View(R.id.txtNomGrafico_r)
    TextView estados;

    @View(R.id.txtPorcentaje)
    TextView porcentaje;

    @View(R.id.txtpresentacion_r)
    TextView nombre;

    Context contexto;
    JSONObject unaEvidencia;
    String base64Image;
    byte[] decodedString;
    Bitmap decodedByte;
    DecoderImagen decoder;

    public emotionR(Context contexto, JSONObject unaEvidencia) {
        this.contexto = contexto;
        this.unaEvidencia = unaEvidencia;
    }

    @Resolve
    protected void onResolved(){ ;
        try{
            int enfadado=0,asqueado=0, temeroso=0, feliz=0, neutral=0, triste=0, sorprendido=0;
            enfadado=unaEvidencia.getInt("enfadado");
            asqueado=unaEvidencia.getInt("asqueado");
            temeroso=unaEvidencia.getInt("temeroso");
            feliz=unaEvidencia.getInt("feliz");
            neutral=unaEvidencia.getInt("neutral");
            triste=unaEvidencia.getInt("triste");
            sorprendido=unaEvidencia.getInt("sorprendido");
            nombre.setText(unaEvidencia.getString("custodiado__persona__nombres")+" - "+unaEvidencia.getString("fecha_inicio_fin"));
            estados.setText("1.Enfadado, 2.Asqueado, 3.Temeroso, 4.Feliz, 5.Neutral, 6.Triste, 7.Sorprendido");
            porcentaje.setText("Prediction percentage "+unaEvidencia.getString("prediccion_trastorno")+"%");
            BarGraphSeries<DataPoint> series=new BarGraphSeries<>(new DataPoint[]{
                    new DataPoint(1,enfadado),
                    new DataPoint(2,asqueado),
                    new DataPoint(3,temeroso),
                    new DataPoint(4,feliz),
                    new DataPoint(5,neutral),
                    new DataPoint(6,triste),
                    new DataPoint(7,sorprendido)
            });
            grafiquita.addSeries(series);
            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    return Color.rgb((int)data.getX()*255/4,(int)data.getY()*255/6,100);
                }
            });
            series.setSpacing(0);
            series.setDrawValuesOnTop(true);
            series.setValuesOnTopColor(Color.BLUE);
        }catch (JSONException ex){
            Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

