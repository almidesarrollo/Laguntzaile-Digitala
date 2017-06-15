package com.example.dam2_yeray.modulomovil;


import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;

import com.example.dam2_yeray.modulomovil.Asyntask.AsynInsert;
import com.example.dam2_yeray.modulomovil.MySqlNiko.MyFunctions;
import com.example.dam2_yeray.modulomovil.PostPhp.PostArray;
import com.example.dam2_yeray.modulomovil.Servicios.ServiceMainMovil;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainWearActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    private Button boton;
    private boolean pulsado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        setAmbientEnabled();
        boton=(Button)findViewById(R.id.btnAyuda);
        pulsado=false;
        startService(new Intent(this, ServiceMainMovil.class));

     /*   mTextView = (TextView) findViewById(R.id.text);*/

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boton.setBackground(getDrawable(R.drawable.colorsitosrojospulsados));

                PostArray array = MyFunctions.crearPost("incidencia");

                array.setValue("cod_incidencia", MyFunctions.INC_AYUD);
                AsynInsert asyn = new AsynInsert(array, MyFunctions.URL_INSERT_LOCATION);
                pulsado=true;
                asyn.execute();



            }
        });
    }


    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if(!pulsado) {
            if (isAmbient()) {

           /* mTextView.setTextColor(getResources().getColor(android.R.color.white));*/

                boton.setBackground(getDrawable(R.drawable.colorsitosrojosapagador));

            } else {

                //   mTextView.setTextColor(getResources().getColor(android.R.color.black));
                boton.setBackground(getDrawable(R.drawable.colorsitosrojos));


            }
        }else
        {
            boton.setBackground(getDrawable(R.drawable.colorsitosrojospulsados));
        }
    }
}
