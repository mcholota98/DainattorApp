package com.example.appbiblioteca;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbiblioteca.Modelos.DecoderImagen;
import com.example.appbiblioteca.Modelos.ImagenBitmap;
import com.example.appbiblioteca.Modelos.people_to_monitor;
import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.Calendar;

public class editCustodiado extends AppCompatActivity implements Asynchtask {
    private ProgressDialog progDailog;
    private TextView txtnombre_edit;
    private TextView txtapellido_edit;
    private TextView txtcedula_edit;
    private TextView txtfecha_nacimiento_edit;
    ImageView imagen_edit;
    EditText etDate_edit;
    private ImagenBitmap decoder;
    DecoderImagen decoder1;
    Bundle bundle;
    String url1;
    int IdCustodio;
    int validador=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custodiado);

        this.txtnombre_edit = (TextView) findViewById(R.id.txtnombres_edit);
        this.txtapellido_edit = (TextView) findViewById(R.id.txtapellidos_edit);
        this.txtcedula_edit = (TextView) findViewById(R.id.txtcedula_edit);
        imagen_edit=(ImageView) findViewById(R.id.imagenUser_edit);
        etDate_edit=findViewById(R.id.txtCalendar_edit);
        url1 = MainActivity.Burl.getString("key1");
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        etDate_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(
                        editCustodiado.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date=year+"-"+month+"-"+day;
                        etDate_edit.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        validador=1;
        bundle = this.getIntent().getExtras();
        IdCustodio=Integer.parseInt(bundle.getString("id"));
        txtnombre_edit.setText(bundle.getString("persona__nombres"));
        txtapellido_edit.setText(bundle.getString("persona__apellidos"));
        txtcedula_edit.setText(bundle.getString("persona__cedula"));
        etDate_edit.setText(bundle.getString("persona__fecha_nacimiento"));
        String str = MainActivity.mMyAppsBundle.getString("key");
        String link_api = url1+"persona/custodiado/?cuidador_id="+str;
        ServicioTask servicioTask = new ServicioTask(this, "GET", link_api, this);
        servicioTask.execute();
    }

    public void aggFotos(View view){
        Intent in=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        in.setType("image/");
        startActivityForResult(in.createChooser(in,"Select the Application"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            imagen_edit.setImageURI(path);
        }
    }

    public void EditCustodiado(View view) {
        try {
            JSONObject json_mensaje = new JSONObject();
            json_mensaje.put("id", Integer.parseInt(bundle.getString("id")));
            json_mensaje.put("cuidador_id", Integer.parseInt(bundle.getString("cuidador_id")));
            json_mensaje.put("persona_id", Integer.parseInt(bundle.getString("persona_id")));
            json_mensaje.put("persona__nombres", txtnombre_edit.getText());
            json_mensaje.put("persona__apellidos", txtapellido_edit.getText());
            json_mensaje.put("persona__cedula", txtcedula_edit.getText());
            json_mensaje.put("persona__fecha_nacimiento", etDate_edit.getText());
            decoder = new ImagenBitmap(this.imagen_edit);
            String ba6 = decoder.getBase64();
            validador=2;
            json_mensaje.put("persona__foto_perfil", "data:image/png;base64," + decoder.getBase64());
            ServicioTask servicioTask = new ServicioTask(this, "PUT", url1 + "persona/custodiado/", json_mensaje.toString(), this);
            servicioTask.execute();
            progDailog = new ProgressDialog(this);
            progDailog.setTitle("Registering custodian");
            progDailog.setMessage("Please wait...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
            txtnombre_edit.setText("");
            txtapellido_edit.setText("");
            txtcedula_edit.setText("");
            etDate_edit.setText("");
            validador=1;
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void processFinish(String result) throws JSONException {
        if(validador==1)
        {
            try {
                JSONObject un_historial;
                JSONObject json_data = new JSONObject(result);
                JSONArray json_historial = json_data.getJSONArray("custodiados");
                for (int i = 0; i < json_historial.length(); i++) {
                    un_historial = json_historial.getJSONObject(i);
                    if (Integer.parseInt(un_historial.getString("id")) == (IdCustodio)) {
                        if (!un_historial.getString("persona__foto_perfil").equals("null")) {
                            decoder1 = new DecoderImagen(un_historial.getString("persona__foto_perfil"));
                            this.imagen_edit.setImageBitmap(decoder1.getImagen());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else {
            try {
                JSONObject json_response = new JSONObject(result);
                progDailog.dismiss();
                String resul = json_response.getString("custodiado");
                System.out.println(resul);
                if (resul.equals("guardado")) {
                    Intent newActivity = new Intent(editCustodiado.this, menu.class);
                    Toast.makeText(this, "Registered caregiver user", Toast.LENGTH_LONG).show();
                    startActivity(newActivity);
                } else {
                    Toast.makeText(this, json_response.get("usuario").toString(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}