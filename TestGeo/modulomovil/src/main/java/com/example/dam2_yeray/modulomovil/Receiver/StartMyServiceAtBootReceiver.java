package com.example.dam2_yeray.modulomovil.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.dam2_yeray.modulomovil.MainWearActivity;
import com.example.dam2_yeray.modulomovil.Servicios.ServiceMainMovil;

/**
 * Created by dam2-yeray on 16/02/2016.
 */
public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, ServiceMainMovil.class);
            context.startService(serviceIntent);
            Intent ActivityIntent=new Intent(context,MainWearActivity.class);

            ActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ActivityIntent);
            Toast.makeText(context,"ENCENDIDO",Toast.LENGTH_LONG).show();
        }
    }
}