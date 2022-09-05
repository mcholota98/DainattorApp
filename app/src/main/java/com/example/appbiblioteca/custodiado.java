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

import com.example.appbiblioteca.Modelos.ImagenBitmap;
import com.example.appbiblioteca.Modelos.people_to_monitor;
import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class custodiado extends AppCompatActivity implements Asynchtask {
    private ProgressDialog progDailog;
    private TextView txtnombre_cus;
    private TextView txtapellido_cus;
    private TextView txtcedula_cus;
    private TextView txtfecha_nacimiento_cus;
    ImageView imagen_cus;
    EditText etDate_cus;
    private ImagenBitmap decoder;
    String url1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custodiado);
        this.txtnombre_cus = (TextView) findViewById(R.id.txtnombres_cus);
        this.txtapellido_cus = (TextView) findViewById(R.id.txtapellidos_cus);
        this.txtcedula_cus = (TextView) findViewById(R.id.txtcedula_cus);
        url1 = MainActivity.Burl.getString("key1");

        imagen_cus=(ImageView) findViewById(R.id.imagenUser_cus);
        etDate_cus=findViewById(R.id.txtCalendar_cus);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        etDate_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(
                        custodiado.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date=year+"-"+month+"-"+day;
                        etDate_cus.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }
    public void aggFoto(View view){
        Intent in=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        in.setType("image/");
        startActivityForResult(in.createChooser(in,"Select the Application"),10);
    }

    public void AggCustodiado(View view) {
        try {
            String str = MainActivity.mMyAppsBundle.getString("key");
            JSONObject json_mensaje = new JSONObject();
            json_mensaje.put("cuidador_id", Integer.parseInt(str));
            json_mensaje.put("persona__nombres", txtnombre_cus.getText());
            json_mensaje.put("persona__apellidos", txtapellido_cus.getText());
            json_mensaje.put("persona__cedula", txtcedula_cus.getText());
            json_mensaje.put("persona__fecha_nacimiento", etDate_cus.getText());
            decoder = new ImagenBitmap(this.imagen_cus);
            String ba64 = decoder.getBase64();
            json_mensaje.put("persona__foto_perfil", "data:image/png;base64," + decoder.getBase64());
            ServicioTask servicioTask = new ServicioTask(this, "POST",url1+"persona/custodiado/", json_mensaje.toString(), this);
            servicioTask.execute();
            progDailog = new ProgressDialog(this);
            progDailog.setTitle("Registering custodian");
            progDailog.setMessage("Please wait...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
            txtnombre_cus.setText("");
            txtapellido_cus.setText("");
            txtcedula_cus.setText("");
            etDate_cus.setText("");
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            imagen_cus.setImageURI(path);
        }
    }

    @Override
    public void processFinish(String result) throws JSONException {
        try {
            JSONObject json_response = new JSONObject(result);
            progDailog.dismiss();
            String resul=json_response.getString("custodiado");
            System.out.println(resul);
            if(resul.equals("guardado")){
                Intent newActivity = new Intent(custodiado.this, menu.class);
                Toast.makeText(this, "Registered caregiver user", Toast.LENGTH_LONG).show();
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